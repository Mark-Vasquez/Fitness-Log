package com.example.fitnesslog.program.ui

import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

data class ProgramsState(
    val programs: List<ProgramWithWorkoutCount> = emptyList(),
    val initializedProgramId: Long? = null,
    val error: String? = null,
)


