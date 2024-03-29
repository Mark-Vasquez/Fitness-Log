package com.example.fitnesslog.program.ui.program

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.SCHEDULED_DAYS
import com.example.fitnesslog.databinding.ModalBottomSheetScheduleSelectBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ScheduleSelectModal : BottomSheetDialogFragment() {

    private var _binding: ModalBottomSheetScheduleSelectBinding? = null
    private val binding: ModalBottomSheetScheduleSelectBinding get() = _binding!!
    private val args: ScheduleSelectModalArgs by navArgs()
    private lateinit var formerScheduledDays: Set<Day>

    companion object {
        const val TAG = "ScheduleSelectModal"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        formerScheduledDays = args.scheduledDays as Set<Day>
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
        val behavior = (dialog as BottomSheetDialog).behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        checkmarkFormerDays()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Pass a Set<Day> back to the parent fragment via savedStateHandle using a key
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            SCHEDULED_DAYS,
            getSelectedDays()
        )
    }

    private fun checkmarkFormerDays() {
        binding.run {
            chipMondayScheduleSelect.isChecked = Day.MONDAY in formerScheduledDays
            chipTuesdayScheduleSelect.isChecked = Day.TUESDAY in formerScheduledDays
            chipWednesdayScheduleSelect.isChecked = Day.WEDNESDAY in formerScheduledDays
            chipThursdayScheduleSelect.isChecked = Day.THURSDAY in formerScheduledDays
            chipFridayScheduleSelect.isChecked = Day.FRIDAY in formerScheduledDays
            chipSaturdayScheduleSelect.isChecked = Day.SATURDAY in formerScheduledDays
            chipSundayScheduleSelect.isChecked = Day.SUNDAY in formerScheduledDays
        }
    }

    private fun getSelectedDays(): Set<Day> {
        val selectedDays = mutableSetOf<Day>()

        binding.run {
            if (chipMondayScheduleSelect.isChecked) selectedDays.add(Day.MONDAY)
            if (chipTuesdayScheduleSelect.isChecked) selectedDays.add(Day.TUESDAY)
            if (chipWednesdayScheduleSelect.isChecked) selectedDays.add(Day.WEDNESDAY)
            if (chipThursdayScheduleSelect.isChecked) selectedDays.add(Day.THURSDAY)
            if (chipFridayScheduleSelect.isChecked) selectedDays.add(Day.FRIDAY)
            if (chipSaturdayScheduleSelect.isChecked) selectedDays.add(Day.SATURDAY)
            if (chipSundayScheduleSelect.isChecked) selectedDays.add(Day.SUNDAY)
        }
        return selectedDays
    }
}