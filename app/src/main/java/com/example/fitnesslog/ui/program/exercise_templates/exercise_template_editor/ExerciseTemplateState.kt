package com.example.fitnesslog.ui.program.exercise_templates.exercise_template_editor

import com.example.fitnesslog.data.entity.ExerciseTemplate

data class ExerciseTemplateState(
    val exerciseTemplate: ExerciseTemplate? = null,
    val error: String? = null
)
