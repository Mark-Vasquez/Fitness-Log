package com.example.fitnesslog.ui.program.programs

import com.example.fitnesslog.data.entity.Program

sealed class ProgramsEvent {
    data class Select(val program: com.example.fitnesslog.domain.model.ProgramWithWorkoutCount) :
        ProgramsEvent()

    data class Delete(val program: Program) : ProgramsEvent()
}
