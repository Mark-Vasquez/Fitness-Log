package com.example.fitnesslog.program.domain.model

import android.os.Parcelable
import com.example.fitnesslog.core.enums.Day
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProgramWithWorkoutCount(
    val id: Int,
    val name: String,
    val scheduledDays: Set<Day>,
    val isSelected: Boolean,
    val restDurationSeconds: Int,
    val workoutCount: Int,
) : Parcelable
