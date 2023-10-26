package com.example.fitnesslog.program.ui.programs

import com.example.fitnesslog.program.data.entity.Program

data class ProgramsState(
    val programs: List<Program> = emptyList(),
    val selectedProgram: Program? = null
)
