package com.example.fitnesslog.ui.program.workout_template

import com.example.fitnesslog.data.entity.WorkoutTemplateExercise

data class WorkoutTemplateExercisesState(
    val workoutTemplateExercises: List<WorkoutTemplateExercise> = emptyList(),
)