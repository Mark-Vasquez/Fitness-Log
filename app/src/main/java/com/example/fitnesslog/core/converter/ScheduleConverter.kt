package com.example.fitnesslog.core.converter

import androidx.room.TypeConverter
import com.example.fitnesslog.core.enums.Day

/**
 * Provides conversion methods for storing and retrieving a Set<String> as a single String in the SQLite database.
 * A pair of inverse conversions must contain a data type not compatible with SQLite that is used
 * in one of the entity fields.
 *
 * - For storing: The set is converted to a comma-separated string.
 * - For retrieval: The comma-separated string is converted back to a Set<String>.
 */
class ScheduleConverter {
    @TypeConverter
    fun fromSet(schedule: Set<Day>): String {
        return schedule.joinToString(",")
    }

    @TypeConverter
    fun toSet(schedule: String): Set<Day> {
        return schedule.split(",")
            .map { Day.valueOf(it) }
            .toSet()
    }
}
