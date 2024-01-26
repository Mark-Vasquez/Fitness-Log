package com.example.fitnesslog.program.ui.program

import com.example.fitnesslog.program.data.entity.WorkoutTemplate

data class WorkoutTemplatesState(
    val workoutTemplates: List<WorkoutTemplate> = emptyList(),
    val error: String? = null
)
