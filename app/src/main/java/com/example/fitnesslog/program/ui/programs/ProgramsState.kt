package com.example.fitnesslog.program.ui.programs

import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

data class ProgramsState(
    val programs: List<ProgramWithWorkoutCount> = emptyList(),
    val selectedProgram: Program? = null,
    val error: String? = null
)
