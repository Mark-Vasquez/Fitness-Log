package com.example.fitnesslog.ui.program.programs.program_create_edit_shared.program_edit

import com.example.fitnesslog.data.entity.Program

data class ProgramEditState(
    val program: Program? = null,
    val error: String? = null,
    val isDeletable: Boolean = false
)
