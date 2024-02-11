package com.example.fitnesslog.ui.program.programs

data class ProgramsState(
    val programs: List<com.example.fitnesslog.domain.model.ProgramWithWorkoutCount> = emptyList(),
    val error: String? = null
)


