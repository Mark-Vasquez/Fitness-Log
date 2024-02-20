package com.example.fitnesslog.ui.program.workout_template

import com.example.fitnesslog.data.entity.WorkoutTemplate

data class WorkoutTemplateState(
    val workoutTemplate: WorkoutTemplate? = null,
    val error: String? = null,
)