package com.example.fitnesslog.program.ui.program_create

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
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.REST_DURATION_SECONDS
import com.example.fitnesslog.core.utils.SCHEDULED_DAYS
import com.example.fitnesslog.core.utils.secondsToMinutesAndSeconds
import com.example.fitnesslog.core.utils.showDiscardDialog
import com.example.fitnesslog.databinding.FragmentProgramCreateBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.Serializable


class ProgramCreateFragment : Fragment() {

    private val programCreateViewModel: ProgramCreateViewModel by viewModels { ProgramCreateViewModel.Factory }
    private var _binding: FragmentProgramCreateBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentProgramCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()


        binding.btnCancelProgramCreate.setOnClickListener {
            showDiscardDialog(
                requireContext()
            ) {
                programCreateViewModel.onEvent(ProgramCreateEvent.Cancel)
                findNavController().popBackStack()
            }
        }


        binding.btnSaveProgramCreate.setOnClickListener {
            programCreateViewModel.onEvent(ProgramCreateEvent.Save)
            findNavController().popBackStack()
        }


        binding.etNameProgramCreate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val name = binding.etNameProgramCreate.text.toString()
                programCreateViewModel.onEvent(ProgramCreateEvent.UpdateName(name))
            }
        })


        binding.btnScheduleProgramCreate.setOnClickListener {
            // To retrieve data back from modal child to this parent fragment via navigation
            handleModalResult<Set<Day>>(
                R.id.programCreateFragment,
                SCHEDULED_DAYS
            ) { scheduledDays ->
                programCreateViewModel.onEvent(ProgramCreateEvent.UpdateScheduledDays(scheduledDays!!))
            }

            // To pass data to the modal child via navigation
            val action =
                ProgramCreateFragmentDirections.actionProgramCreateFragmentToScheduleSelectModal(
                    scheduledDays = programCreateViewModel.stateFlow.value.scheduledDays as Serializable
                )
            findNavController().navigate(action)
        }


        binding.btnRestTimeProgramCreate.setOnClickListener {
            handleModalResult<Int>(
                R.id.programCreateFragment,
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
                    restDurationSeconds = programCreateViewModel.stateFlow.value.restDurationSeconds
                )
            findNavController().navigate(action)
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programCreateViewModel.stateFlow.collect { state ->
                    state.error?.let {
                        Snackbar.make(
                            requireView(),
                            it,
                            Snackbar.LENGTH_LONG
                        )
                    }

                    updateScheduledDaysView(state.scheduledDays)
                    updateRestDurationView(state.restDurationSeconds)
                }
            }
        }
    }

    private fun updateScheduledDaysView(scheduledDays: Set<Day>) {
        if (scheduledDays.size == 1) {
            binding.tvScheduledDaysProgramCreate.text = scheduledDays.first().value
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
        binding.tvScheduledDaysProgramCreate.text = selectedScheduleString.joinToString("/")
    }

    private fun updateRestDurationView(seconds: Int) {
        val (minutes, seconds) = secondsToMinutesAndSeconds(seconds)
        val durationString =
            if (minutes == 0) "${seconds}s"
            else if (seconds == 0) "${minutes}m"
            else "${minutes}m${seconds}s"
        binding.tvRestTimeProgramCreate.text = durationString
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
                programCreateViewModel.onEvent(
                    ProgramCreateEvent.Cancel
                )
                findNavController().popBackStack()
            }


        }
    }
}