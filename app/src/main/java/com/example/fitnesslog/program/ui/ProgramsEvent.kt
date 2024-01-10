package com.example.fitnesslog.program.ui

import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

sealed class ProgramsEvent {
    data object ShowCreateForm : ProgramsEvent()
    data class ShowEditForm(val program: ProgramWithWorkoutCount) : ProgramsEvent()
    data class SaveCreate(val program: Program) : ProgramsEvent()
    data class CancelCreate(val program: Program) : ProgramsEvent()
    data class Select(val program: ProgramWithWorkoutCount) : ProgramsEvent()
    data class Edit(val program: Program) : ProgramsEvent()
    data class Delete(val program: Program) : ProgramsEvent()
}
