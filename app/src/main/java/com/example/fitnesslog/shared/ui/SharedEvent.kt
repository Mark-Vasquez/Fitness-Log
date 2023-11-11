package com.example.fitnesslog.shared.ui

import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

sealed class SharedEvent {
    data class SelectProgram(val program: ProgramWithWorkoutCount) : SharedEvent()
}
