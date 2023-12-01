package com.example.fitnesslog.program.ui.programs

import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

sealed class ProgramsEvent {
    data object ShowCreateForm : ProgramsEvent()
    data class ShowEditForm(val program: ProgramWithWorkoutCount) : ProgramsEvent()
    data class Create(val program: ProgramWithWorkoutCount) : ProgramsEvent()
    data class Select(val program: ProgramWithWorkoutCount) : ProgramsEvent()
    data class Edit(val program: ProgramWithWorkoutCount) : ProgramsEvent()
    data class Delete(val program: ProgramWithWorkoutCount) : ProgramsEvent()
}
