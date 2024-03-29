package com.example.fitnesslog.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fitnesslog.core.converter.ExerciseEnumConverter
import com.example.fitnesslog.core.converter.ScheduleConverter
import com.example.fitnesslog.exercise.data.dao.ExerciseDao
import com.example.fitnesslog.exercise.data.dao.SetDao
import com.example.fitnesslog.exercise.data.entity.ExerciseTemplate
import com.example.fitnesslog.exercise.data.entity.SetTemplate
import com.example.fitnesslog.exercise.data.entity.WorkoutTemplateExercise
import com.example.fitnesslog.history.data.dao.HistoryDao
import com.example.fitnesslog.history.data.entity.WorkoutSession
import com.example.fitnesslog.history.data.entity.WorkoutSessionExercise
import com.example.fitnesslog.history.data.entity.WorkoutSessionExerciseSet
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.workout.data.dao.WorkoutDao
import com.example.fitnesslog.workout.data.entity.WorkoutTemplate

@Database(
    entities = [
        Program::class,
        WorkoutTemplate::class,
        WorkoutTemplateExercise::class,
        ExerciseTemplate::class,
        SetTemplate::class,
        WorkoutSession::class,
        WorkoutSessionExercise::class,
        WorkoutSessionExerciseSet::class
    ],
    version = 1, exportSchema = true
)
@TypeConverters(ScheduleConverter::class, ExerciseEnumConverter::class)
abstract class FitnessLogDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "fitness_log.db"
    }

    abstract fun programDao(): ProgramDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun setDao(): SetDao
    abstract fun historyDao(): HistoryDao
}