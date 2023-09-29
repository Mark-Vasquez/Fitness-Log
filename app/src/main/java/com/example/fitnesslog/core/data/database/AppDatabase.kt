package com.example.fitnesslog.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fitnesslog.exercise.data.dao.ExerciseDao
import com.example.fitnesslog.exercise.data.dao.SetDao
import com.example.fitnesslog.exercise.domain.model.ExerciseTemplate
import com.example.fitnesslog.exercise.domain.model.SetTemplate
import com.example.fitnesslog.exercise.domain.model.WorkoutSessionExercise
import com.example.fitnesslog.exercise.domain.model.WorkoutSessionExerciseSet
import com.example.fitnesslog.exercise.domain.model.WorkoutTemplateExercise
import com.example.fitnesslog.history.data.dao.HistoryDao
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.domain.model.Program
import com.example.fitnesslog.workout.data.dao.WorkoutDao
import com.example.fitnesslog.workout.domain.model.WorkoutSession
import com.example.fitnesslog.workout.domain.model.WorkoutTemplate

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