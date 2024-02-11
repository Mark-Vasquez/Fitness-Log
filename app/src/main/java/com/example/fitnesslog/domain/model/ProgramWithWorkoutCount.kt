package com.example.fitnesslog.domain.model

import com.example.fitnesslog.core.enums.Day

data class ProgramWithWorkoutCount(
    val id: Int,
    val name: String,
    val scheduledDays: Set<Day>,
    val isSelected: Boolean,
    val restDurationSeconds: Int,
    val workoutCount: Int,
)
