package com.example.fitnesslog.workout.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.fitnesslog.program.domain.model.Program


@Entity(
    tableName = "workout_template",
    indices = [Index(value = ["program_id", "position"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Program::class,
            parentColumns = ["id"],
            childColumns = ["program_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkoutTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "program_id") val programId: Int,
    @ColumnInfo(name = "position") val position: Int,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)