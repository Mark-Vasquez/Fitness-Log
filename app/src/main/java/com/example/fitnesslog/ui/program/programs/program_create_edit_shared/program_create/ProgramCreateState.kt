package com.example.fitnesslog.ui.program.programs.program_create_edit_shared.program_create

import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.data.entity.Program

data class ProgramCreateState(
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
)
