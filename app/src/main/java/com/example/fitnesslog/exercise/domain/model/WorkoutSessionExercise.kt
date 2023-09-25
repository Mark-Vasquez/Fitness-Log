package com.example.fitnesslog.exercise.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.fitnesslog.workout.domain.model.WorkoutSession

/**
 * Represents an exercise within a specific workout session.
 *
 * Data from the exercise template is copied over, rather than directly referenced,
 * to preserve the integrity of historical workouts. This ensures that any modifications
 * or deletions to the original template do not retroactively affect past sessions.
 *
 * Example: Deleted referenced templates will cause the session in history to lose name of exercise
 */

@Entity(
    tableName = "workout_session_exercise",
    indices = [Index(value = ["workout_session_id", "position"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSession::class,
            parentColumns = ["id"],
            childColumns = ["workout_session_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutSessionExercise(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "workout_session_id") val workoutSessionId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
