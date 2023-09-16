package com.example.fitnesslog.exercise.data.model

data class WorkoutExerciseWithTemplateName(
    val id: Int,
    val name: String,
    val workoutTemplateId: Int,
    val exerciseTemplateId: Int,
    val position: Int,
    val createdAt: Long,
    val updatedAt: Long
)
