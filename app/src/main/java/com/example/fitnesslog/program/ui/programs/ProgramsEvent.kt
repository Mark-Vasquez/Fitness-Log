package com.example.fitnesslog.program.ui.programs

import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

sealed class ProgramsEvent {
    data class Select(val program: ProgramWithWorkoutCount) : ProgramsEvent()
    data class Delete(val program: Program) : ProgramsEvent()
}
