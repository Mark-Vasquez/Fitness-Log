package com.example.fitnesslog.program.ui.program_create

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.SCHEDULED_DAYS
import com.example.fitnesslog.databinding.ModalBottomSheetScheduleSelectBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ScheduleSelectModal : BottomSheetDialogFragment() {

    private var _binding: ModalBottomSheetScheduleSelectBinding? = null
    private val binding: ModalBottomSheetScheduleSelectBinding get() = _binding!!

    companion object {
        const val TAG = "ScheduleSelectModal"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ModalBottomSheetScheduleSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            SCHEDULED_DAYS,
            getSelectedDays()
        )
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