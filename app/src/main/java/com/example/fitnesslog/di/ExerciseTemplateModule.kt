package com.example.fitnesslog.di

import com.example.fitnesslog.core.database.FitnessLogDatabase
import com.example.fitnesslog.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.data.repository.ExerciseRepositoryImpl

interface ExerciseTemplateModule {
    val exerciseTemplateUseCases: com.example.fitnesslog.domain.use_case.exercise_template.ExerciseTemplateUseCases
    val exerciseTemplateDao: ExerciseTemplateDao
    val exerciseRepository: com.example.fitnesslog.domain.repository.ExerciseRepository
}

class ExerciseTemplateModuleImpl(private val db: FitnessLogDatabase) : ExerciseTemplateModule {
    override val exerciseTemplateDao: ExerciseTemplateDao by lazy {
        db.exerciseTemplateDao()
    }
    override val exerciseRepository: com.example.fitnesslog.domain.repository.ExerciseRepository by lazy {
        ExerciseRepositoryImpl(exerciseTemplateDao)
    }

    override val exerciseTemplateUseCases: com.example.fitnesslog.domain.use_case.exercise_template.ExerciseTemplateUseCases by lazy {
        com.example.fitnesslog.domain.use_case.exercise_template.ExerciseTemplateUseCases(
            createExerciseTemplate = com.example.fitnesslog.domain.use_case.exercise_template.CreateExerciseTemplate(
                exerciseRepository
            ),
            getExerciseTemplates = com.example.fitnesslog.domain.use_case.exercise_template.GetExerciseTemplates(
                exerciseRepository
            ),
            editExerciseTemplate = com.example.fitnesslog.domain.use_case.exercise_template.EditExerciseTemplate(
                exerciseRepository
            ),
            tryDeleteExerciseTemplate = com.example.fitnesslog.domain.use_case.exercise_template.TryDeleteExerciseTemplate(
                exerciseRepository
            ),
            addExercisesToWorkoutTemplate = com.example.fitnesslog.domain.use_case.exercise_template.AddExercisesToWorkoutTemplate(
                exerciseRepository
            ),
            getExercisesForWorkoutTemplate = com.example.fitnesslog.domain.use_case.exercise_template.GetExercisesForWorkoutTemplate(
                exerciseRepository
            ),
            reorderExercisesForWorkoutTemplate = com.example.fitnesslog.domain.use_case.exercise_template.ReorderExercisesForWorkoutTemplate(
                exerciseRepository
            ),
            deleteExerciseFromWorkoutTemplate = com.example.fitnesslog.domain.use_case.exercise_template.DeleteExerciseFromWorkoutTemplate(
                exerciseRepository
            )
        )
    }
}