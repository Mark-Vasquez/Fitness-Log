package com.example.fitnesslog.program.ui.program

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
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.REST_DURATION_SECONDS
import com.example.fitnesslog.core.utils.SCHEDULED_DAYS
import com.example.fitnesslog.core.utils.secondsToMinutesAndSeconds
import com.example.fitnesslog.core.utils.setDebouncedOnClickListener
import com.example.fitnesslog.core.utils.showDeleteDialog
import com.example.fitnesslog.core.utils.showDiscardDialog
import com.example.fitnesslog.databinding.FragmentProgramBinding
import com.example.fitnesslog.program.ui.ProgramMode
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.io.Serializable


class ProgramFragment : Fragment() {
    private val programViewModel: ProgramViewModel by viewModels { ProgramViewModel.Factory }
    private var _binding: FragmentProgramBinding? = null
    private val binding get() = _binding!!
    private val args: ProgramFragmentArgs by navArgs()

    companion object {
        const val TAG = "ProgramFragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Only initialize the viewModel when Fragment is created for the first time
        if (savedInstanceState == null) {
            when (args.programMode) {
                ProgramMode.CREATE -> programViewModel.onEvent(ProgramEvent.CreateMode(args.programMode))
                ProgramMode.EDIT -> programViewModel.onEvent(
                    ProgramEvent.EditMode(
                        args.programMode,
                        args.programId
                    )
                )
            }
        }
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
        setupUIViews()
        observeViewModelState()

        binding.btnCancelProgram.setOnClickListener {
            showDiscardDialog(requireContext()) {
                programViewModel.onEvent(ProgramEvent.Cancel)
                findNavController().popBackStack()
            }
        }

        binding.btnSaveProgram.setOnClickListener {
            programViewModel.onEvent(ProgramEvent.Save)
            findNavController().popBackStack()
        }

        binding.etNameProgram.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val name = binding.etNameProgram.text.toString()
                programViewModel.onEvent(ProgramEvent.UpdateName(name))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Debounce to eliminate trying to navigate to same location twice
        binding.btnScheduleProgram.setDebouncedOnClickListener {
            // To retrieve data back from modal child to this parent fragment via navigation
            handleModalResult<Set<Day>>(R.id.programFragment, SCHEDULED_DAYS) { scheduledDays ->
                programViewModel.onEvent(ProgramEvent.UpdateScheduledDays(scheduledDays!!))
            }
            // To pass data to the modal child via navigation
            val action =
                ProgramFragmentDirections.actionProgramFragmentToScheduleSelectModal(
                    scheduledDays = programViewModel.stateFlow.value.scheduledDays as Serializable
                )
            findNavController().navigate(action)
        }

        binding.btnRestTimeProgram.setDebouncedOnClickListener {
            handleModalResult<Int>(
                R.id.programFragment,
                REST_DURATION_SECONDS
            ) { restDurationSeconds ->
                programViewModel.onEvent(
                    ProgramEvent.UpdateRestDurationSeconds(
                        restDurationSeconds!!
                    )
                )
            }
            val action =
                ProgramFragmentDirections.actionProgramFragmentToRestTimeSelectDialog(
                    restDurationSeconds = programViewModel.stateFlow.value.restDurationSeconds
                )
            findNavController().navigate(action)
        }

        binding.btnDeleteProgram.setOnClickListener {
            if (args.programMode != ProgramMode.EDIT) {
                return@setOnClickListener
            }

            val isDeletable = programViewModel.stateFlow.value.isDeletable
            if (isDeletable) {
                val programName = programViewModel.stateFlow.value.program?.name
                val message = if (programName.isNullOrEmpty()) {
                    "Are you sure you want to delete this program?"
                } else {
                    "Are you sure you want to delete \"${programName}\" program?"
                }
                showDeleteDialog(requireContext(), message) {
                    programViewModel.onEvent(ProgramEvent.Delete)
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

    private fun setupUIViews() = when (args.programMode) {
        ProgramMode.CREATE -> {
            binding.tvTitleProgram.text = getString(R.string.create_program)
        }

        ProgramMode.EDIT -> {
            binding.tvTitleProgram.text = getString(R.string.edit_program)
            binding.btnDeleteProgram.visibility = View.VISIBLE
        }
    }

    private fun observeViewModelState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programViewModel.stateFlow.collect { state ->
                    updateNameInputView(state.name)
                    updateScheduledDaysView(state.scheduledDays)
                    updateRestDurationView(state.restDurationSeconds)
                    Log.d(TAG, "${state}")
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
                programViewModel.onEvent(
                    ProgramEvent.Cancel
                )
                findNavController().popBackStack()
            }
        }
    }
}