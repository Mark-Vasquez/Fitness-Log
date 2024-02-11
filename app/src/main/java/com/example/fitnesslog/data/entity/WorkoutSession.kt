package com.example.fitnesslog.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * A workout_session row copies the name field from a workout_template and uses that template_id
 * to create the exercise and sets sessions from the corresponding templates. If that template
 * gets deleted, we just set the foreignKeys to null because the template id no longer exists
 */

@Entity(
    tableName = "workout_session",
    indices = [Index(value = ["workout_template_id"])],
    foreignKeys = [ForeignKey(
        entity = WorkoutTemplate::class,
        parentColumns = ["id"],
        childColumns = ["workout_template_id"],
        onDelete = ForeignKey.SET_NULL
    )]
)
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "workout_template_id") val workoutTemplateId: Int?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_completed", defaultValue = "0") val isCompleted: Boolean = false,
    @ColumnInfo(name = "duration_minutes") val durationMinutes: Int? = null,
    @ColumnInfo(name = "started_at") val startedAt: Long? = null,
    @ColumnInfo(name = "completed_at") val completedAt: Long? = null,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
