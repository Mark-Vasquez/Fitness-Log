package com.example.fitnesslog.program.di

import com.example.fitnesslog.core.data.database.FitnessLogDatabase
import com.example.fitnesslog.program.data.dao.WorkoutTemplateDao
import com.example.fitnesslog.program.data.repository.WorkoutRepositoryImpl
import com.example.fitnesslog.program.domain.repository.WorkoutRepository
import com.example.fitnesslog.program.domain.use_case.workout_template.CreateWorkoutTemplate
import com.example.fitnesslog.program.domain.use_case.workout_template.DeleteWorkoutTemplate
import com.example.fitnesslog.program.domain.use_case.workout_template.EditWorkoutTemplate
import com.example.fitnesslog.program.domain.use_case.workout_template.GetWorkoutTemplates
import com.example.fitnesslog.program.domain.use_case.workout_template.ReorderWorkoutTemplate
import com.example.fitnesslog.program.domain.use_case.workout_template.WorkoutTemplateUseCases

interface WorkoutTemplateModule {
    val workoutTemplateUseCases: WorkoutTemplateUseCases
    val workoutTemplateDao: WorkoutTemplateDao
}

class WorkoutTemplateModuleImpl(private val db: FitnessLogDatabase) : WorkoutTemplateModule {
    override val workoutTemplateDao: WorkoutTemplateDao by lazy {
        db.workoutDao()
    }

    private val workoutRepository: WorkoutRepository by lazy {
        WorkoutRepositoryImpl(workoutTemplateDao)
    }
    override val workoutTemplateUseCases: WorkoutTemplateUseCases by lazy {
        WorkoutTemplateUseCases(
            createWorkoutTemplate = CreateWorkoutTemplate(workoutRepository),
            getWorkoutTemplates = GetWorkoutTemplates(workoutRepository),
            editWorkoutTemplate = EditWorkoutTemplate(workoutRepository),
            reorderWorkoutTemplate = ReorderWorkoutTemplate(workoutRepository),
            deleteWorkoutTemplate = DeleteWorkoutTemplate(workoutRepository)
        )
    }

}