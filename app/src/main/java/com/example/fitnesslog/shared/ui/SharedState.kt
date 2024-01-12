package com.example.fitnesslog.shared.ui

import com.example.fitnesslog.program.data.entity.Program

data class SharedState(
    val selectedProgram: Program? = null,
    val error: String? = null
)
