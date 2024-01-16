package com.example.fitnesslog.program.ui.program_create

sealed class ProgramCreateEvent {
    data object Save : ProgramCreateEvent()
    data object Cancel : ProgramCreateEvent()
}