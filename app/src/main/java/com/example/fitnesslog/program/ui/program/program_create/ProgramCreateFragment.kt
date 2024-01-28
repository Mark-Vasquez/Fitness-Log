package com.example.fitnesslog.program.ui.program.program_create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.REST_DURATION_SECONDS
import com.example.fitnesslog.core.utils.SCHEDULED_DAYS
import com.example.fitnesslog.core.utils.setDebouncedOnClickListener
import com.example.fitnesslog.core.utils.showDiscardDialog
import com.example.fitnesslog.databinding.FragmentProgramBinding
import com.example.fitnesslog.program.ui.program.WorkoutTemplatesAdapter
import com.example.fitnesslog.program.ui.program.handleModalResult
import com.example.fitnesslog.program.ui.program.updateNameInputView
import com.example.fitnesslog.program.ui.program.updateRestDurationView
import com.example.fitnesslog.program.ui.program.updateScheduledDaysView
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
        binding.tvTitleProgram.text = getString(R.string.create_program)
        binding.btnNavigateBack.text = getString(R.string.cancel)
        binding.btnDeleteProgram.visibility = View.GONE
        setupRecyclerView()
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
                val name = binding.etNameProgram.text.toString()
                programCreateViewModel.onEvent(ProgramCreateEvent.UpdateName(name))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Debounce to eliminate trying to navigate to same location twice
        binding.btnScheduleProgram.setDebouncedOnClickListener {
            val navBackStackEntry =
                findNavController().getBackStackEntry(R.id.programCreateFragment)
            // To retrieve data back from modal child to this parent fragment via navigation
            handleModalResult<Set<Day>>(
                navBackStackEntry,
                viewLifecycleOwner,
                SCHEDULED_DAYS
            ) { scheduledDays ->
                programCreateViewModel.onEvent(ProgramCreateEvent.UpdateScheduledDays(scheduledDays!!))
            }
            // To pass data to the modal child via navigation
            val action =
                ProgramCreateFragmentDirections.actionProgramCreateFragmentToScheduleSelectModal(
                    scheduledDays = programCreateViewModel.programState.value.scheduledDays as Serializable
                )
            findNavController().navigate(action)
        }

        binding.btnRestTimeProgram.setDebouncedOnClickListener {
            val navBackStackEntry =
                findNavController().getBackStackEntry(R.id.programCreateFragment) // reference the new xml
            handleModalResult<Int>(
                navBackStackEntry,
                viewLifecycleOwner,
                REST_DURATION_SECONDS
            ) { restDurationSeconds ->
                programCreateViewModel.onEvent(
                    ProgramCreateEvent.UpdateRestDurationSeconds(
                        restDurationSeconds!!
                    )
                )
            }
            val action =
                ProgramCreateFragmentDirections.actionProgramCreateFragmentToRestTimeSelectDialog(
                    restDurationSeconds = programCreateViewModel.programState.value.restDurationSeconds
                )
            findNavController().navigate(action)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupRecyclerView() {
        val rvWorkoutTemplates = binding.rvWorkoutTemplates
        rvWorkoutTemplates.layoutManager = LinearLayoutManager(requireContext())
        workoutTemplatesAdapter = WorkoutTemplatesAdapter()
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
                val swipeFlags = ItemTouchHelper.LEFT
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

                if (oldIndex < newIndex) {
                    for (i in oldIndex until newIndex) {
                        Collections.swap(updatedList, i, i + 1)
                    }
                } else {
                    for (i in oldIndex downTo newIndex + 1) {
                        Collections.swap(updatedList, i, i - 1)
                    }
                }
                adapter.submitList(updatedList)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d(TAG, "${adapter.currentList[viewHolder.adapterPosition]}")
            }

        }

        val itemTouchHelper =
            ItemTouchHelper(ItemTouchHelperCallback(adapter = workoutTemplatesAdapter))
        itemTouchHelper.attachToRecyclerView(rvWorkoutTemplates)

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
}