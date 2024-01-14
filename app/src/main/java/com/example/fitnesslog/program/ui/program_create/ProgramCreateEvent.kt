package com.example.fitnesslog.program.ui.program_create

import com.example.fitnesslog.program.data.entity.Program

sealed class ProgramCreateEvent {
    data class Save(val program: Program) : ProgramCreateEvent()
    data class Cancel(val programId: Long) : ProgramCreateEvent()
}