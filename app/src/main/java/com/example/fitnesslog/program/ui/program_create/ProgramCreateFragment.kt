package com.example.fitnesslog.program.ui.program_create

import android.os.Bundle
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
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.showDiscardDialog
import com.example.fitnesslog.databinding.FragmentProgramCreateBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


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

        binding.btnProgramCreateCancel.setOnClickListener {
            showDiscardDialog(
                requireContext()
            ) {
                programCreateViewModel.onEvent(ProgramCreateEvent.Cancel)
                findNavController().popBackStack()
            }
        }

        binding.btnProgramCreateSave.setOnClickListener {
            val name = binding.etProgramName.text.toString()
            val scheduledDays = getSelectedDays()
//            val restDurationSeconds = binding.
            programCreateViewModel.updateProgramData(name, scheduledDays)
            programCreateViewModel.onEvent(ProgramCreateEvent.Save)
            findNavController().popBackStack()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programCreateViewModel.stateFlow.collect { programCreateState ->
                    programCreateState.error?.let {
                        Snackbar.make(
                            requireView(),
                            it,
                            Snackbar.LENGTH_LONG
                        )
                    }
                }
            }
        }
    }


    private fun getSelectedDays(): Set<Day> {
        val selectedDays = mutableSetOf<Day>()

        binding.run {
            if (chipMonday.isChecked) selectedDays.add(Day.MONDAY)
            if (chipTuesday.isChecked) selectedDays.add(Day.TUESDAY)
            if (chipWednesday.isChecked) selectedDays.add(Day.WEDNESDAY)
            if (chipThursday.isChecked) selectedDays.add(Day.THURSDAY)
            if (chipFriday.isChecked) selectedDays.add(Day.FRIDAY)
            if (chipSaturday.isChecked) selectedDays.add(Day.SATURDAY)
            if (chipSunday.isChecked) selectedDays.add(Day.SUNDAY)
        }
        return selectedDays
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