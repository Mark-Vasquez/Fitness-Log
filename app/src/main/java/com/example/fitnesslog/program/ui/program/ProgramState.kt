package com.example.fitnesslog.program.ui.program

import com.example.fitnesslog.core.enums.Day

data class ProgramState(
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
