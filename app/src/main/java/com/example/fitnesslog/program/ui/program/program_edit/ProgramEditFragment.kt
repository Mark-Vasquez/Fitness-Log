package com.example.fitnesslog.program.ui.program.program_edit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
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
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.FitnessLogApp.Companion.workoutTemplateModule
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.REST_DURATION_SECONDS
import com.example.fitnesslog.core.utils.SCHEDULED_DAYS
import com.example.fitnesslog.core.utils.secondsToMinutesAndSeconds
import com.example.fitnesslog.core.utils.setDebouncedOnClickListener
import com.example.fitnesslog.core.utils.showDeleteDialog
import com.example.fitnesslog.core.utils.showDiscardDialog
import com.example.fitnesslog.databinding.FragmentProgramBinding
import com.example.fitnesslog.program.ui.program.WorkoutTemplatesAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Serializable
import java.util.Collections


class ProgramEditFragment : Fragment() {
    private val programEditViewModel: ProgramEditViewModel by viewModels {
        ProgramEditViewModel.Companion.Factory(
            args.programId,
            programModule.programUseCases,
            workoutTemplateModule.workoutTemplateUseCases
        )
    }
    private var _binding: FragmentProgramBinding? = null
    private val binding get() = _binding!!
    private val args: ProgramEditFragmentArgs by navArgs()
    private lateinit var workoutTemplatesAdapter: WorkoutTemplatesAdapter

    companion object {
        const val TAG = "ProgramEditFragment"
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
            compoundDrawablePadding = 4
        }
        setupRecyclerView()
        observeProgramState()
        observeWorkoutTemplatesState()

        binding.btnNavigateBack.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.etNameProgram.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val name = binding.etNameProgram.text.toString()
                programEditViewModel.onEvent(ProgramEditEvent.UpdateName(name))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Debounce to eliminate trying to navigate to same location twice
        binding.btnScheduleProgram.setDebouncedOnClickListener {
            // To retrieve data back from modal child to this parent fragment via navigation
            handleModalResult<Set<Day>>(R.id.programEditFragment, SCHEDULED_DAYS) { scheduledDays ->
                programEditViewModel.onEvent(ProgramEditEvent.UpdateScheduledDays(scheduledDays!!))
            }
            // To pass data to the modal child via navigation
            val action =
                ProgramEditFragmentDirections.actionProgramEditFragmentToScheduleSelectModal(
                    scheduledDays = programEditViewModel.programState.value.scheduledDays as Serializable
                )
            findNavController().navigate(action)
        }

        binding.btnRestTimeProgram.setDebouncedOnClickListener {
            handleModalResult<Int>(
                R.id.programEditFragment,
                REST_DURATION_SECONDS
            ) { restDurationSeconds ->
                programEditViewModel.onEvent(
                    ProgramEditEvent.UpdateRestDurationSeconds(
                        restDurationSeconds!!
                    )
                )
            }
            val action =
                ProgramEditFragmentDirections.actionProgramEditFragmentToRestTimeSelectDialog(
                    restDurationSeconds = programEditViewModel.programState.value.restDurationSeconds
                )
            findNavController().navigate(action)
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
                showDeleteDialog(requireContext(), message) {
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
                programEditViewModel.programState.collect { state ->
                    updateNameInputView(state.name)
                    updateScheduledDaysView(state.scheduledDays)
                    updateRestDurationView(state.restDurationSeconds)
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


    private fun updateNameInputView(name: String) {
        // This allow the text input to only populate on initial Edit or on configuration changes
        if (binding.etNameProgram.text.toString() != name) {
            binding.etNameProgram.setText(name)
            binding.etNameProgram.setSelection(binding.etNameProgram.length())
        }
    }


    private fun updateScheduledDaysView(scheduledDays: Set<Day>) {
        if (scheduledDays.size == 1) {
            binding.tvScheduledDaysProgram.text = scheduledDays.first().value
            return
        }
        val selectedScheduleString = mutableListOf<String>()
        if (Day.MONDAY in scheduledDays) selectedScheduleString.add("Mo")
        if (Day.TUESDAY in scheduledDays) selectedScheduleString.add("Tu")
        if (Day.WEDNESDAY in scheduledDays) selectedScheduleString.add("We")
        if (Day.THURSDAY in scheduledDays) selectedScheduleString.add("Th")
        if (Day.FRIDAY in scheduledDays) selectedScheduleString.add("Fr")
        if (Day.SATURDAY in scheduledDays) selectedScheduleString.add("Sa")
        if (Day.SUNDAY in scheduledDays) selectedScheduleString.add("Su")
        binding.tvScheduledDaysProgram.text = selectedScheduleString.joinToString("/")
    }

    private fun updateRestDurationView(seconds: Int) {
        val (minutes, seconds) = secondsToMinutesAndSeconds(seconds)
        val durationString =
            if (minutes == 0) "${seconds}s"
            else if (seconds == 0) "${minutes}m"
            else "${minutes}m${seconds}s"
        binding.tvRestTimeProgram.text = durationString
    }

    private fun <T> handleModalResult(
        currDestination: Int,
        savedStateHandleKey: String,
        handleResult: (T?) -> Unit
    ) {
        /** NavBackStackEntry is a lifecycleOwner reference to a destination on the nav backstack
         *  that provides a lifecycle, viewmodelStore, and savedStateHandle. Allows the navBackStack
         *  to manage the same viewModel and state in memory that the destination fragment owns
         */
        val navBackStackEntry =
            findNavController().getBackStackEntry(currDestination)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(savedStateHandleKey)
            ) {
                val result = navBackStackEntry.savedStateHandle.get<T>(savedStateHandleKey);
                // Do something with the result via the passed in lambda
                handleResult(result)
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private fun setBackButtonListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showDiscardDialog(
                requireContext()
            ) {
                findNavController().popBackStack()
            }
        }
    }
}