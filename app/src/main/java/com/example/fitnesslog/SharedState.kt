package com.example.fitnesslog

import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

data class SharedState(
    val selectedProgram: ProgramWithWorkoutCount? = null,
    val error: String? = null
)
