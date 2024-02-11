package com.example.fitnesslog.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.fitnesslog.core.converter.ScheduleConverter
import com.example.fitnesslog.core.enums.Day

@Entity(tableName = "program")
@TypeConverters(ScheduleConverter::class)
data class Program(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name", defaultValue = "") val name: String = "",
    @ColumnInfo(
        name = "scheduled_days",
        defaultValue = "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY"
    ) val scheduledDays: Set<Day> = setOf(
        Day.MONDAY,
        Day.TUESDAY,
        Day.WEDNESDAY,
        Day.THURSDAY,
        Day.FRIDAY
    ),
    @ColumnInfo(name = "is_selected", defaultValue = "0") val isSelected: Boolean = false,
    @ColumnInfo(
        name = "rest_duration_seconds",
        defaultValue = "90"
    ) val restDurationSeconds: Int = 90,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)


