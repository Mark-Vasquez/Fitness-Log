package com.example.fitnesslog.program.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

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

/**
 * Provides conversion methods for storing and retrieving a Set<String> as a single String in the SQLite database.
 *
 * - For storing: The set is converted to a comma-separated string.
 * - For retrieval: The comma-separated string is converted back to a Set<String>.
 */
class ScheduleConverter {
    @TypeConverter
    fun fromSet(scheduleSet: Set<String>): String = scheduleSet.joinToString(",")

    @TypeConverter
    fun fromString(scheduleString: String): Set<String> = scheduleString.split(",").toSet()
}
