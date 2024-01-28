package com.example.fitnesslog.program.ui.program.program_edit

import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.ui.ProgramMode

data class ProgramEditState(
    val programMode: ProgramMode = ProgramMode.CREATE,
    val program: Program? = null,
    val name: String = "",
    val scheduledDays: Set<Day> = setOf(
        Day.MONDAY,
        Day.TUESDAY,
        Day.WEDNESDAY,
        Day.THURSDAY,
        Day.FRIDAY
    ),
    val restDurationSeconds: Int = 90,
    val error: String? = null,
    val isDeletable: Boolean = false
)
