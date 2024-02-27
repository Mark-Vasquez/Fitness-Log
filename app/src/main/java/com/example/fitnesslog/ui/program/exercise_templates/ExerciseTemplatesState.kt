package com.example.fitnesslog.ui.program.exercise_templates

import com.example.fitnesslog.data.entity.ExerciseTemplate

data class ExerciseTemplatesState(
    val exerciseTemplates: List<ExerciseTemplate> = emptyList(),
    val exerciseTemplateCheckedMap: HashMap<Int, Boolean> = hashMapOf(),
    val error: String? = null,
)