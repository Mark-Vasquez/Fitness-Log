package com.example.fitnesslog.core.converter

import androidx.room.TypeConverter
import com.example.fitnesslog.core.enums.ExerciseMuscle
import com.example.fitnesslog.core.enums.ExerciseResistance

class ExerciseEnumConverter {
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