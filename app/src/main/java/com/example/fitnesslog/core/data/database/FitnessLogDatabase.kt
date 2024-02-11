package com.example.fitnesslog.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.fitnesslog.core.converter.ExerciseEnumConverter
import com.example.fitnesslog.core.converter.ScheduleConverter
import com.example.fitnesslog.program.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.data.dao.WorkoutTemplateDao
import com.example.fitnesslog.program.data.dao.WorkoutTemplateExerciseDao
import com.example.fitnesslog.program.data.dao.WorkoutTemplateExerciseSetDao
import com.example.fitnesslog.program.data.entity.ExerciseTemplate
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.program.data.entity.WorkoutTemplateExercise
import com.example.fitnesslog.program.data.entity.WorkoutTemplateExerciseSet
import com.example.fitnesslog.workout.data.dao.WorkoutSessionDao
import com.example.fitnesslog.workout.data.dao.WorkoutSessionExerciseDao
import com.example.fitnesslog.workout.data.dao.WorkoutSessionExerciseSetDao
import com.example.fitnesslog.workout.data.entity.WorkoutSession
import com.example.fitnesslog.workout.data.entity.WorkoutSessionExercise
import com.example.fitnesslog.workout.data.entity.WorkoutSessionExerciseSet

@Database(
    entities = [
        Program::class,
        ExerciseTemplate::class,
        WorkoutTemplate::class,
        WorkoutTemplateExercise::class,
        WorkoutTemplateExerciseSet::class,
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
    abstract fun exerciseTemplateDao(): ExerciseTemplateDao
    abstract fun workoutTemplateDao(): WorkoutTemplateDao
    abstract fun workoutTemplateExerciseDao(): WorkoutTemplateExerciseDao
    abstract fun workoutTemplateExerciseSetDao(): WorkoutTemplateExerciseSetDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun workoutSessionExerciseDao(): WorkoutSessionExerciseDao
    abstract fun workoutSessionExerciseSeDao(): WorkoutSessionExerciseSetDao

}