package com.example.fitnesslog

import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

sealed class SharedEvent {
    data class SelectProgram(val program: ProgramWithWorkoutCount) : SharedEvent()
}
