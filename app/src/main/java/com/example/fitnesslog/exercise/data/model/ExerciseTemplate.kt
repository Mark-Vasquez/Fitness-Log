package com.example.fitnesslog.exercise.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.fitnesslog.core.enums.ExerciseMuscle
import com.example.fitnesslog.core.enums.ExerciseResistance

@Entity(
    tableName = "exercise_template"
)
@TypeConverters(ExerciseEnumConverters::class)
data class ExerciseTemplate(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "exercise_muscle") val exerciseMuscle: ExerciseMuscle,
    @ColumnInfo(name = "exercise_resistance") val exerciseResistance: ExerciseResistance,
    @ColumnInfo(name = "is_default", defaultValue = "0") val isDefault: Boolean = false,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)


class ExerciseEnumConverters {
    @TypeConverter
    fun fromExerciseMuscle(exerciseMuscle: ExerciseMuscle): String = exerciseMuscle.name

    @TypeConverter
    fun toExerciseMuscle(exerciseMuscle: String): ExerciseMuscle =
        ExerciseMuscle.valueOf(exerciseMuscle)

    @TypeConverter
    fun fromExerciseResistance(exerciseResistance: ExerciseResistance): String =
        exerciseResistance.name

    @TypeConverter
    fun toExerciseResistance(exerciseResistance: String): ExerciseResistance =
        ExerciseResistance.valueOf(exerciseResistance)
}