package com.example.fitnesslog.exercise.domain.model

data class WorkoutTemplateExerciseWithName(
    val workoutTemplateExerciseId: Int,
    val name: String,
    val workoutTemplateId: Int,
    val exerciseTemplateId: Int,
    val position: Int,
    val createdAt: Long,
    val updatedAt: Long
)
