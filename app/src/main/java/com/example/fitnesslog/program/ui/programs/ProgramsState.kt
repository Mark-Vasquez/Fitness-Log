package com.example.fitnesslog.program.ui.programs

import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

data class ProgramsState(
    val programs: List<ProgramWithWorkoutCount> = emptyList(),
    val error: String? = null,
    val modalEvent: ProgramModalEvent? = null
)

sealed class ProgramModalEvent {
    data object ShowCreateForm : ProgramModalEvent()
    data class EditCreateForm(val program: ProgramWithWorkoutCount) : ProgramModalEvent()
}