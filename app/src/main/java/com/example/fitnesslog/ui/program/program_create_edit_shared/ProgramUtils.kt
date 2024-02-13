package com.example.fitnesslog.ui.program.program_create_edit_shared

import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.helpers.secondsToMinutesAndSeconds
import com.example.fitnesslog.databinding.FragmentProgramBinding

fun updateNameInputView(binding: FragmentProgramBinding, name: String) {
    // This allow the text input to only populate on initial Edit or on configuration changes
    if (binding.etNameProgram.text.toString() != name) {
        binding.etNameProgram.setText(name)
        binding.etNameProgram.setSelection(binding.etNameProgram.length())
    }
}

fun updateScheduledDaysView(binding: FragmentProgramBinding, scheduledDays: Set<Day>) {
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

fun updateRestDurationView(binding: FragmentProgramBinding, seconds: Int) {
    val (minutes, seconds) = secondsToMinutesAndSeconds(seconds)
    val durationString =
        if (minutes == 0) "${seconds}s"
        else if (seconds == 0) "${minutes}m"
        else "${minutes}m${seconds}s"
    binding.tvRestTimeProgram.text = durationString
}



