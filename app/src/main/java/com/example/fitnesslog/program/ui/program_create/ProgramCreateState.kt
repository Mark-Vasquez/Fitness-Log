package com.example.fitnesslog.program.ui.program_create

import com.example.fitnesslog.core.enums.Day

data class ProgramCreateState(
    val initializedProgramId: Int? = null,
    val name: String = "",
    val scheduledDays: Set<Day> = setOf(
        Day.MONDAY,
        Day.TUESDAY,
        Day.WEDNESDAY,
        Day.THURSDAY,
        Day.FRIDAY
    ),
    val restDurationSeconds: Int = 90,
    val error: String? = null
)
