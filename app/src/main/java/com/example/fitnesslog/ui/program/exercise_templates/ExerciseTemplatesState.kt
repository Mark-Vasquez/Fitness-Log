package com.example.fitnesslog.ui.program.exercise_templates

import com.example.fitnesslog.data.entity.ExerciseTemplate

data class ExerciseTemplatesState(
    val exerciseTemplates: List<ExerciseTemplate> = emptyList(),
    val error: String? = null,
)