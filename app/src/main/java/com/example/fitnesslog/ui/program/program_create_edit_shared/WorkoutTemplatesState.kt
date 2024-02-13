package com.example.fitnesslog.ui.program.program_create_edit_shared

import com.example.fitnesslog.data.entity.WorkoutTemplate

data class WorkoutTemplatesState(
    val workoutTemplates: List<WorkoutTemplate> = emptyList(),
)
