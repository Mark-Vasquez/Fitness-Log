package com.example.fitnesslog.workout.di

import com.example.fitnesslog.core.data.database.FitnessLogDatabase
import com.example.fitnesslog.workout.data.repository.WorkoutRepositoryImpl
import com.example.fitnesslog.workout.domain.repository.WorkoutRepository
import com.example.fitnesslog.workout.domain.use_case.CreateWorkoutTemplate
import com.example.fitnesslog.workout.domain.use_case.DeleteWorkoutTemplate
import com.example.fitnesslog.workout.domain.use_case.EditWorkoutTemplate
import com.example.fitnesslog.workout.domain.use_case.GetWorkoutTemplates
import com.example.fitnesslog.workout.domain.use_case.ReorderWorkoutTemplate
import com.example.fitnesslog.workout.domain.use_case.WorkoutUseCases

interface WorkoutModule {
    val workoutRepository: WorkoutRepository
    val workoutUseCases: WorkoutUseCases
}

class WorkoutModuleImpl(private val db: FitnessLogDatabase) : WorkoutModule {
    override val workoutRepository: WorkoutRepository by lazy {
        WorkoutRepositoryImpl(db.workoutDao())
    }
    override val workoutUseCases: WorkoutUseCases by lazy {
        WorkoutUseCases(
            createWorkoutTemplate = CreateWorkoutTemplate(workoutRepository),
            getWorkoutTemplates = GetWorkoutTemplates(workoutRepository),
            editWorkoutTemplate = EditWorkoutTemplate(workoutRepository),
            reorderWorkoutTemplate = ReorderWorkoutTemplate(workoutRepository),
            deleteWorkoutTemplate = DeleteWorkoutTemplate(workoutRepository)
        )
    }

}