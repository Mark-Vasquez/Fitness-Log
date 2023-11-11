package com.example.fitnesslog.program.ui.programs

import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

data class ProgramsState(
    val programs: List<ProgramWithWorkoutCount> = emptyList(),
    val error: String? = null
)
