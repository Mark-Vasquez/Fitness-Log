package com.example.fitnesslog.program.ui.program

import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.program.ui.ProgramMode

sealed class ProgramEvent {
    data class EditMode(val mode: ProgramMode, val programId: Int) : ProgramEvent()
    data class CreateMode(val mode: ProgramMode) : ProgramEvent()
    data object Save : ProgramEvent()
    data object Cancel : ProgramEvent()
    data class UpdateName(val name: String) : ProgramEvent()
    data class UpdateScheduledDays(val scheduledDays: Set<Day>) : ProgramEvent()
    data class UpdateRestDurationSeconds(val restDurationSeconds: Int) : ProgramEvent()
    data object Delete : ProgramEvent()
}