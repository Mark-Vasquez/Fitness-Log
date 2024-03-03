package com.example.fitnesslog.ui.program.programs.program_create_edit_shared.program_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.constants.REST_DURATION_SECONDS
import com.example.fitnesslog.core.utils.constants.SCHEDULED_DAYS
import com.example.fitnesslog.core.utils.extensions.setDebouncedOnClickListener
import com.example.fitnesslog.core.utils.extensions.textChangeFlow
import com.example.fitnesslog.core.utils.ui.showDeleteDialog
import com.example.fitnesslog.data.entity.WorkoutTemplate
import com.example.fitnesslog.databinding.FragmentProgramBinding
import com.example.fitnesslog.ui.program.programs.program_create_edit_shared.WorkoutTemplatesAdapter
import com.example.fitnesslog.ui.program.programs.program_create_edit_shared.updateNameInputView
import com.example.fitnesslog.ui.program.programs.program_create_edit_shared.updateRestDurationView
import com.example.fitnesslog.ui.program.programs.program_create_edit_shared.updateScheduledDaysView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.Collections


class ProgramEditFragment : Fragment() {
    private val programEditViewModel: ProgramEditViewModel by viewModels {
        ProgramEditViewModel.Factory(
            args.programId,
            appModule.programUseCases,
            appModule.workoutTemplateUseCases
        )
    }
    private var _binding: FragmentProgramBinding? = null
    private val binding get() = _binding!!
    private val args: ProgramEditFragmentArgs by navArgs()
    private lateinit var workoutTemplatesAdapter: WorkoutTemplatesAdapter

    companion object {
        const val TAG = "ProgramEditFragment"
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
        setupProgramNameChangeListener()


        // Debounce to eliminate trying to navigate to same location twice
        binding.btnScheduleProgram.setDebouncedOnClickListener {
            // To pass data to the modal child via navigation
            val action =
                ProgramEditFragmentDirections.actionProgramEditFragmentToScheduleSelectModal(
                    scheduledDays = programEditViewModel.programState.value.program?.scheduledDays as Serializable
                )
            findNavController().navigate(action)
        }

        binding.btnRestTimeProgram.setDebouncedOnClickListener {
            val action =
                ProgramEditFragmentDirections.actionProgramEditFragmentToRestTimeSelectDialog(
                    restDurationSeconds = programEditViewModel.programState.value.program?.restDurationSeconds as Int
                )
            findNavController().navigate(action)
        }

        binding.fabAddWorkout.setOnClickListener {
            programEditViewModel.onEvent(ProgramEditEvent.CreateWorkoutTemplate)
        }

        binding.btnNavigateBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnDeleteProgram.setOnClickListener {
            val isDeletable = programEditViewModel.programState.value.isDeletable
            if (isDeletable) {
                val programName = programEditViewModel.programState.value.program?.name
                val message = if (programName.isNullOrEmpty()) {
                    "Are you sure you want to delete this program?"
                } else {
                    "Are you sure you want to delete \"${programName}\" program?"
                }
                showDeleteDialog(requireContext(), "Delete Program", message) {
                    programEditViewModel.onEvent(ProgramEditEvent.Delete)
                    findNavController().popBackStack()
                }
            } else {
                // show cannot delete dialog
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Cannot Delete")
                    .setMessage("Must have at least one Program selected.")
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun observeProgramState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programEditViewModel.programState.collect { state ->
                    state.program?.let { program ->
                        updateNameInputView(binding, program.name)
                        updateScheduledDaysView(binding, program.scheduledDays)
                        updateRestDurationView(binding, program.restDurationSeconds)
                    }
                }
            }
        }
    }

    private fun observeWorkoutTemplatesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programEditViewModel.workoutTemplatesState.collectLatest { workoutTemplatesState ->
                    workoutTemplatesAdapter.submitList(workoutTemplatesState.workoutTemplates)
                }
            }
        }
    }

    private fun setupProgramNameChangeListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.etNameProgram.textChangeFlow()
                    .debounce(500)
                    .collectLatest { name ->
                        programEditViewModel.onEvent(ProgramEditEvent.UpdateName(name))
                    }
            }
        }
    }

    private fun setupUI() {
        binding.tvTitleProgram.text = getString(R.string.edit_program)
        binding.btnDeleteProgram.visibility = View.VISIBLE
        binding.btnSaveProgram.visibility = View.GONE
        binding.btnNavigateBack.apply {
            text = getString(R.string.back)
            val backArrowIcon = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.baseline_arrow_back_ios_new_24
            )
            setCompoundDrawablesWithIntrinsicBounds(backArrowIcon, null, null, null)
        }
    }

    private fun setupNavBackStackEntryObservers() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.programEditFragment)
        val scheduleSelectObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(SCHEDULED_DAYS)
            ) {
                val scheduledDays =
                    navBackStackEntry.savedStateHandle.get<Set<Day>>(SCHEDULED_DAYS);
                if (scheduledDays != null) {
                    // Do something with the passed in data from modal
                    programEditViewModel.onEvent(
                        ProgramEditEvent.UpdateScheduledDays(
                            scheduledDays
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
                    programEditViewModel.onEvent(
                        ProgramEditEvent.UpdateRestDurationSeconds(
                            restDurationSeconds
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

    private fun setupRecyclerView() {
        val rvWorkoutTemplates = binding.rvWorkoutTemplates
        rvWorkoutTemplates.layoutManager = LinearLayoutManager(requireContext())
        workoutTemplatesAdapter =
            WorkoutTemplatesAdapter(object : WorkoutTemplatesAdapter.WorkoutTemplateClickListener {
                override fun onWorkoutTemplateClicked(workoutTemplate: WorkoutTemplate) {
                    if (workoutTemplate.id == null) return
                    val action =
                        ProgramEditFragmentDirections.actionProgramEditFragmentToWorkoutTemplateFragment(
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
                    // Make semi-transparent on drag
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                // Take away transparency after finish dragging and dropped
                viewHolder.itemView.alpha = 1.0f

                val updatedList = adapter.currentList
                programEditViewModel.onEvent(
                    ProgramEditEvent.UpdateWorkoutTemplatesOrder(
                        updatedList
                    )
                )
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        }

        val itemTouchHelper =
            ItemTouchHelper(ItemTouchHelperCallback(adapter = workoutTemplatesAdapter))
        itemTouchHelper.attachToRecyclerView(rvWorkoutTemplates)

    }
}