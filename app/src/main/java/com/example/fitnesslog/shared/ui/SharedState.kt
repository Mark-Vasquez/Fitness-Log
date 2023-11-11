package com.example.fitnesslog.shared.ui

import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

data class SharedState(
    val selectedProgram: ProgramWithWorkoutCount? = null,
    val error: String? = null
)
