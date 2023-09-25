package com.example.fitnesslog.program.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.fitnesslog.core.converter.ScheduleConverter

@Entity(tableName = "program")
@TypeConverters(ScheduleConverter::class)
data class Program(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "scheduled_days") val scheduledDays: Set<String>,
    @ColumnInfo(name = "is_selected", defaultValue = "0") val isSelected: Boolean = false,
    @ColumnInfo(
        name = "rest_duration_seconds",
        defaultValue = "90"
    ) val restDurationSeconds: Int = 90,
    // TODO: set default to System.currentTimeMillis() in actual insertion/update, not instantiation
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)


