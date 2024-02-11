package com.example.fitnesslog.di

import com.example.fitnesslog.core.database.FitnessLogDatabase
import com.example.fitnesslog.data.dao.WorkoutTemplateDao
import com.example.fitnesslog.data.repository.WorkoutRepositoryImpl

interface WorkoutTemplateModule {
    val workoutTemplateUseCases: com.example.fitnesslog.domain.use_case.workout_template.WorkoutTemplateUseCases
    val workoutTemplateDao: WorkoutTemplateDao
    val workoutRepository: com.example.fitnesslog.domain.repository.WorkoutRepository
}

class WorkoutTemplateModuleImpl(private val db: FitnessLogDatabase) : WorkoutTemplateModule {
    override val workoutTemplateDao: WorkoutTemplateDao by lazy {
        db.workoutTemplateDao()
    }

    override val workoutRepository: com.example.fitnesslog.domain.repository.WorkoutRepository by lazy {
        WorkoutRepositoryImpl(workoutTemplateDao)
    }
    override val workoutTemplateUseCases: com.example.fitnesslog.domain.use_case.workout_template.WorkoutTemplateUseCases by lazy {
        com.example.fitnesslog.domain.use_case.workout_template.WorkoutTemplateUseCases(
            createWorkoutTemplate = com.example.fitnesslog.domain.use_case.workout_template.CreateWorkoutTemplate(
                workoutRepository
            ),
            getWorkoutTemplates = com.example.fitnesslog.domain.use_case.workout_template.GetWorkoutTemplates(
                workoutRepository
            ),
            editWorkoutTemplate = com.example.fitnesslog.domain.use_case.workout_template.EditWorkoutTemplate(
                workoutRepository
            ),
            reorderWorkoutTemplates = com.example.fitnesslog.domain.use_case.workout_template.ReorderWorkoutTemplates(
                workoutRepository
            ),
            deleteWorkoutTemplate = com.example.fitnesslog.domain.use_case.workout_template.DeleteWorkoutTemplate(
                workoutRepository
            ),
            getWorkoutTemplate = com.example.fitnesslog.domain.use_case.workout_template.GetWorkoutTemplate(
                workoutRepository
            )
        )
    }

}