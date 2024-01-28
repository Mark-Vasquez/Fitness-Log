package com.example.fitnesslog.program.ui.program.program_create

import com.example.fitnesslog.core.enums.Day

sealed class ProgramCreateEvent {
    data object Save : ProgramCreateEvent()
    data object Cancel : ProgramCreateEvent()
    data class UpdateName(val name: String) : ProgramCreateEvent()
    data class UpdateScheduledDays(val scheduledDays: Set<Day>) : ProgramCreateEvent()
    data class UpdateRestDurationSeconds(val restDurationSeconds: Int) : ProgramCreateEvent()
}