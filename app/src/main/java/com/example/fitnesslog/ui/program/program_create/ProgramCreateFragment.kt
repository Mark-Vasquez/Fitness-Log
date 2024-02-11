package com.example.fitnesslog.ui.program.program_create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.constants.REST_DURATION_SECONDS
import com.example.fitnesslog.core.utils.constants.SCHEDULED_DAYS
import com.example.fitnesslog.core.utils.extensions.setDebouncedOnClickListener
import com.example.fitnesslog.core.utils.ui.showDiscardDialog
import com.example.fitnesslog.data.entity.WorkoutTemplate
import com.example.fitnesslog.databinding.FragmentProgramBinding
import com.example.fitnesslog.ui.program.WorkoutTemplatesAdapter
import com.example.fitnesslog.ui.program.updateNameInputView
import com.example.fitnesslog.ui.program.updateRestDurationView
import com.example.fitnesslog.ui.program.updateScheduledDaysView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.Collections

class ProgramCreateFragment : Fragment() {
    private val programCreateViewModel: ProgramCreateViewModel by viewModels { ProgramCreateViewModel.Factory }
    private var _binding: FragmentProgramBinding? = null
    private val binding get() = _binding!!
    private lateinit var workoutTemplatesAdapter: WorkoutTemplatesAdapter

    companion object {
        const val TAG = "ProgramCreateFragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackButtonListener()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProgramBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecyclerView()
        setupNavBackStackEntryObservers()
        observeProgramState()
        observeWorkoutTemplatesState()

        binding.btnNavigateBack.setOnClickListener {
            showDiscardDialog(requireContext()) {
                programCreateViewModel.onEvent(ProgramCreateEvent.Cancel)
                findNavController().popBackStack()
            }
        }

        binding.btnSaveProgram.setOnClickListener {
            programCreateViewModel.onEvent(ProgramCreateEvent.Save)
            findNavController().popBackStack()
        }

        binding.etNameProgram.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                programCreateViewModel.onEvent(ProgramCreateEvent.UpdateName(s.toString()))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        // Debounce to eliminate trying to navigate to same location twice
        binding.btnScheduleProgram.setDebouncedOnClickListener {
            // To pass data to the modal child via navigation
            val action =
                ProgramCreateFragmentDirections.actionProgramCreateFragmentToScheduleSelectModal(
                    scheduledDays = programCreateViewModel.programState.value.scheduledDays as Serializable
                )
            findNavController().navigate(action)
        }

        binding.btnRestTimeProgram.setDebouncedOnClickListener {
            val action =
                ProgramCreateFragmentDirections.actionProgramCreateFragmentToRestTimeSelectDialog(
                    restDurationSeconds = programCreateViewModel.programState.value.restDurationSeconds
                )
            findNavController().navigate(action)
        }

        binding.fabAddWorkout.setOnClickListener {
            programCreateViewModel.onEvent(ProgramCreateEvent.CreateWorkoutTemplate)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupUI() {
        binding.tvTitleProgram.text = getString(R.string.create_program)
        binding.btnNavigateBack.text = getString(R.string.cancel)
        binding.btnDeleteProgram.visibility = View.GONE
    }

    private fun setupNavBackStackEntryObservers() {
        val navBackStackEntry =
            findNavController().getBackStackEntry(R.id.programCreateFragment)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val scheduleSelectObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(SCHEDULED_DAYS)
            ) {
                val scheduledDays =
                    navBackStackEntry.savedStateHandle.get<Set<Day>>(SCHEDULED_DAYS);
                if (scheduledDays != null) {
                    // Do something with the passed in data from modal
                    programCreateViewModel.onEvent(
                        ProgramCreateEvent.UpdateScheduledDays(
                            scheduledDays!!
                        )
                    )
                }
            }
        }
        val restTimeSelectObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(REST_DURATION_SECONDS)
            ) {
                val restDurationSeconds =
                    navBackStackEntry.savedStateHandle.get<Int>(REST_DURATION_SECONDS);
                if (restDurationSeconds != null) {
                    programCreateViewModel.onEvent(
                        ProgramCreateEvent.UpdateRestDurationSeconds(
                            restDurationSeconds!!
                        )
                    )
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(scheduleSelectObserver)
        navBackStackEntry.lifecycle.addObserver(restTimeSelectObserver)

        // addObserver() for the fragment lifecycle on the navBackStack does not automatically remove the observer
        // Call removeObserver() on the navBackStackEntry lifecycle manually when the view lifecycle is destroyed
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(scheduleSelectObserver)
                navBackStackEntry.lifecycle.removeObserver(restTimeSelectObserver)
            }
        })
    }

    private fun observeProgramState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programCreateViewModel.programState.collect { state ->
                    updateNameInputView(binding, state.name)
                    updateScheduledDaysView(binding, state.scheduledDays)
                    updateRestDurationView(binding, state.restDurationSeconds)
                }
            }
        }
    }

    private fun observeWorkoutTemplatesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programCreateViewModel.workoutTemplatesState.collectLatest { workoutTemplatesState ->
                    workoutTemplatesAdapter.submitList(workoutTemplatesState.workoutTemplates)
                }
            }
        }
    }


    private fun setBackButtonListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showDiscardDialog(
                requireContext()
            ) {
                programCreateViewModel.onEvent(
                    ProgramCreateEvent.Cancel
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun setupRecyclerView() {
        val rvWorkoutTemplates = binding.rvWorkoutTemplates
        rvWorkoutTemplates.layoutManager = LinearLayoutManager(requireContext())
        workoutTemplatesAdapter =
            WorkoutTemplatesAdapter(object : WorkoutTemplatesAdapter.WorkoutTemplateClickListener {
                override fun onWorkoutTemplateClicked(workoutTemplate: WorkoutTemplate) {
                    if (workoutTemplate.id == null) return
                    val action =
                        ProgramCreateFragmentDirections.actionProgramCreateFragmentToWorkoutTemplateFragment(
                            workoutTemplateId = workoutTemplate.id
                        )
                    findNavController().navigate(action)
                }
            })
        rvWorkoutTemplates.adapter = workoutTemplatesAdapter

        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.apply {
            isLastItemDecorated = false
        }
        rvWorkoutTemplates.addItemDecoration(divider)

        class ItemTouchHelperCallback(private val adapter: WorkoutTemplatesAdapter) :
            ItemTouchHelper.Callback() {

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = 0
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val oldIndex = viewHolder.adapterPosition
                val newIndex = target.adapterPosition
                val updatedList = adapter.currentList.toMutableList()
                Collections.swap(updatedList, oldIndex, newIndex)
                adapter.submitList(updatedList)
                return true
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}


            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1.0f

                val updatedList = workoutTemplatesAdapter.currentList
                programCreateViewModel.onEvent(
                    ProgramCreateEvent.UpdateWorkoutTemplatesOrder(
                        updatedList
                    )
                )
            }
        }

        val itemTouchHelper =
            ItemTouchHelper(ItemTouchHelperCallback(adapter = workoutTemplatesAdapter))
        itemTouchHelper.attachToRecyclerView(rvWorkoutTemplates)

    }
}