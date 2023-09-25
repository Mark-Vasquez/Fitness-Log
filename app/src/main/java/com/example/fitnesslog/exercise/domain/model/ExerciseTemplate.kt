package com.example.fitnesslog.exercise.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.fitnesslog.core.converter.ExerciseEnumConverter
import com.example.fitnesslog.core.enums.ExerciseMuscle
import com.example.fitnesslog.core.enums.ExerciseResistance

@Entity(
    tableName = "exercise_template"
)
@TypeConverters(ExerciseEnumConverter::class)
data class ExerciseTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "exercise_muscle") val exerciseMuscle: ExerciseMuscle,
    @ColumnInfo(name = "exercise_resistance") val exerciseResistance: ExerciseResistance,
    @ColumnInfo(name = "is_default", defaultValue = "0") val isDefault: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)

