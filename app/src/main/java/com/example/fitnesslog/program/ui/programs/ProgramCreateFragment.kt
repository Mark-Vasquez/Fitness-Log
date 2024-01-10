package com.example.fitnesslog.program.ui.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.showDiscardDialog
import com.example.fitnesslog.databinding.FragmentProgramCreateBinding
import com.example.fitnesslog.program.data.entity.Program
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class ProgramCreateFragment : Fragment() {

    private val programsViewModel: ProgramsViewModel by activityViewModels { ProgramsViewModel.Factory }
    private var _binding: FragmentProgramCreateBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProgramCreateBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_program_create, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()

        binding.tvProgramModalCancel.setOnClickListener {
            showDiscardDialog(requireContext(), {})
        }

        binding.tvProgramModalSave.setOnClickListener {
            saveProgramData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programsViewModel.stateFlow.collect { programState ->
                    programState.error?.let {
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

    private fun saveProgramData() {
        // TODO: check whether save on edit or create
        val name = binding.etProgramName.text.toString()
        val program = Program(
            name = name,
            scheduledDays = getSelectedDays(),
            restDurationSeconds = 90,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        programsViewModel.onEvent(ProgramsEvent.Create(program))
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
}