package com.example.fitnesslog.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Represents a set template and its unique position within a specific workout_template_exercise.
 */
@Entity(
    tableName = "workout_template_exercise_set",
    indices = [Index(value = ["workout_template_exercise_id", "position"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutTemplateExercise::class,
            parentColumns = ["id"],
            childColumns = ["workout_template_exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutTemplateExerciseSet(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "workout_template_exercise_id") val workoutTemplateExerciseId: Int,
    @ColumnInfo(name = "goal_reps") val goalReps: Int,
    @ColumnInfo(name = "weight_in_lbs") val weightInLbs: Int,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
)