package com.example.fitnesslog.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitnesslog.exercise.data.dao.ExerciseDao
import com.example.fitnesslog.exercise.data.dao.SetDao
import com.example.fitnesslog.exercise.data.entity.ExerciseTemplate
import com.example.fitnesslog.exercise.data.entity.SetTemplate
import com.example.fitnesslog.exercise.data.entity.WorkoutSessionExercise
import com.example.fitnesslog.exercise.data.entity.WorkoutSessionExerciseSet
import com.example.fitnesslog.exercise.data.entity.WorkoutTemplateExercise
import com.example.fitnesslog.history.data.dao.HistoryDao
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.workout.data.dao.WorkoutDao
import com.example.fitnesslog.workout.data.entity.WorkoutSession
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
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun programDao(): ProgramDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun setDao(): SetDao
    abstract fun historyDao(): HistoryDao
}