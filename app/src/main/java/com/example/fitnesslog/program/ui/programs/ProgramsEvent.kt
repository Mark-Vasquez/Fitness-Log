package com.example.fitnesslog.program.ui.programs

import com.example.fitnesslog.program.data.entity.Program

sealed class ProgramsEvent {
    data object Create : ProgramsEvent()
    data class Select(val program: Program) : ProgramsEvent()
    data class Edit(val program: Program) : ProgramsEvent()
    data class Delete(val program: Program) : ProgramsEvent()
}
