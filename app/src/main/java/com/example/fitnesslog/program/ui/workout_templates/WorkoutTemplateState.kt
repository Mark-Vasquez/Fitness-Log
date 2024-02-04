package com.example.fitnesslog.program.ui.workout_templates

import com.example.fitnesslog.program.data.entity.WorkoutTemplate

data class WorkoutTemplateState(
    val workoutTemplate: WorkoutTemplate? = null,
    val error: String? = null,
)
