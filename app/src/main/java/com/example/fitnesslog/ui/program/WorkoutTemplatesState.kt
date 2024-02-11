package com.example.fitnesslog.ui.program

import com.example.fitnesslog.data.entity.WorkoutTemplate

data class WorkoutTemplatesState(
    val workoutTemplates: List<WorkoutTemplate> = emptyList(),
)
