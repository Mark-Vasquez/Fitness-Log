package com.example.fitnesslog.program.ui.program.program_edit

import com.example.fitnesslog.core.enums.Day

sealed class ProgramEditEvent {

    data class UpdateName(val name: String) : ProgramEditEvent()
    data class UpdateScheduledDays(val scheduledDays: Set<Day>) : ProgramEditEvent()
    data class UpdateRestDurationSeconds(val restDurationSeconds: Int) : ProgramEditEvent()
    data object Delete : ProgramEditEvent()
}