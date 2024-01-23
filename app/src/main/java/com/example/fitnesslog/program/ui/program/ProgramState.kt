package com.example.fitnesslog.program.ui.program

import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.ui.ProgramMode

data class ProgramState(
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
    val snackbarMessage: String? = null

)
