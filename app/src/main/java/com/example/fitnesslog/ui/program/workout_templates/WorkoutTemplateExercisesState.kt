package com.example.fitnesslog.ui.program.workout_templates

data class WorkoutTemplateExercisesState(
    val workoutTemplateExercises: List<com.example.fitnesslog.domain.model.WorkoutTemplateExerciseWithName> = emptyList(),
)