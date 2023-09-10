package com.example.fitnesslog.exercise.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.fitnesslog.workout.data.model.WorkoutTemplate

/**
 * Represents the association between a workout template and its exercises.
 *
 * [`workout_template_id`, `position`] is enforced as unique pair
 * using the `@Index` annotation, ensuring that within a specific workout template,
 * exercises at any position are distinct.
 *
 * The unique index on these columns provides quick lookups and sorting (by storing sorted rows)
 * without the need for a composite primary key.
 *
 */
@Entity(
    tableName = "workout_template_exercise",
    indices = [Index(value = ["workout_template_id", "position"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = WorkoutTemplate::class,
            parentColumns = ["id"],
            childColumns = ["workout_template_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExerciseTemplate::class,
            parentColumns = ["id"],
            childColumns = ["exercise_template_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutTemplateExercise(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "workout_template_id") val workoutTemplateId: Int,
    @ColumnInfo(name = "exercise_template_id") val exerciseTemplateId: Int,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
