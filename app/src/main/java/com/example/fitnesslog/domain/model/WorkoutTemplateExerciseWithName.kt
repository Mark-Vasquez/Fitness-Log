package com.example.fitnesslog.domain.model

data class WorkoutTemplateExerciseWithName(
    val id: Int,
    val name: String,
    val workoutTemplateId: Int,
    val exerciseTemplateId: Int,
    val position: Int,
    val createdAt: Long,
    val updatedAt: Long
)
