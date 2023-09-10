package com.example.fitnesslog.set.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.fitnesslog.exercise.data.model.ExerciseTemplate

@Entity(
    tableName = "set_template",
    foreignKeys = [
        ForeignKey(
            entity = ExerciseTemplate::class,
            parentColumns = ["id"],
            childColumns = ["exercise_template_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SetTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "exercise_template_id") val exerciseTemplateId: Int,
    @ColumnInfo(name = "goal_reps") val goalReps: Int,
    @ColumnInfo(name = "weight_in_lbs") val weightInLbs: Int,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long,
)
