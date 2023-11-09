package com.example.fitnesslog.program.ui.programs

import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

sealed class ProgramsEvent {
    data object Create : ProgramsEvent()
    data class Select(val program: ProgramWithWorkoutCount) : ProgramsEvent()
    data class Edit(val program: ProgramWithWorkoutCount) : ProgramsEvent()
    data class Delete(val program: ProgramWithWorkoutCount) : ProgramsEvent()
}
