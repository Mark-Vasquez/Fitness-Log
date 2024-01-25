package com.example.fitnesslog.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fitnesslog.core.converter.ExerciseEnumConverter
import com.example.fitnesslog.core.converter.ScheduleConverter
import com.example.fitnesslog.program.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.data.dao.SetTemplateDao
import com.example.fitnesslog.program.data.dao.WorkoutTemplateDao
import com.example.fitnesslog.program.data.entity.ExerciseTemplate
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.data.entity.SetTemplate
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.program.data.entity.WorkoutTemplateExercise
import com.example.fitnesslog.workout.data.dao.WorkoutSessionDao
import com.example.fitnesslog.workout.data.entity.WorkoutSession
import com.example.fitnesslog.workout.data.entity.WorkoutSessionExercise
import com.example.fitnesslog.workout.data.entity.WorkoutSessionExerciseSet

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
    abstract fun workoutDao(): WorkoutTemplateDao
    abstract fun exerciseDao(): ExerciseTemplateDao
    abstract fun setDao(): SetTemplateDao
    abstract fun historyDao(): WorkoutSessionDao
}