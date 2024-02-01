package com.example.fitnesslog.program.ui.program.program_edit

import com.example.fitnesslog.program.data.entity.Program

data class ProgramEditState(
    val program: Program? = null,
    val error: String? = null,
    val isDeletable: Boolean = false
)
