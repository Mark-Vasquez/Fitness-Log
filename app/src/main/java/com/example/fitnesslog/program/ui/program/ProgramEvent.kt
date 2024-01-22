package com.example.fitnesslog.program.ui.program

import com.example.fitnesslog.core.enums.Day

sealed class ProgramEvent {
    data object Save : ProgramEvent()
    data object Cancel : ProgramEvent()
    data class UpdateName(val name: String) : ProgramEvent()
    data class UpdateScheduledDays(val scheduledDays: Set<Day>) : ProgramEvent()
    data class UpdateRestDurationSeconds(val restDurationSeconds: Int) : ProgramEvent()
}