package com.example.fitnesslog.workout.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a set within a specific exercise in a workout session.
 */

@Entity(
    tableName = "workout_session_exercise_set",
    indices = [Index(value = ["workout_session_exercise_id", "position"])],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionExercise::class,
            parentColumns = ["id"],
            childColumns = ["workout_session_exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutSessionExerciseSet(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "workout_session_exercise_id") val workoutSessionExerciseId: Int,
    @ColumnInfo(name = "goal_reps") val goalReps: Int,
    @ColumnInfo(name = "completed_reps", defaultValue = "0") val completedReps: Int = 0,
    @ColumnInfo(name = "weight_in_lbs") val weightInLbs: Int,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
