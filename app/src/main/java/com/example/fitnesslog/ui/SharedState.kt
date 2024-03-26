package com.example.fitnesslog.ui

import com.example.fitnesslog.data.entity.Program

data class SharedState(
    val selectedProgram: Program? = null,
    val error: String? = null
)
