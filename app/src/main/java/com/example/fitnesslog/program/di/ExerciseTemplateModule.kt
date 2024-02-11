package com.example.fitnesslog.program.di

import com.example.fitnesslog.core.data.database.FitnessLogDatabase
import com.example.fitnesslog.program.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.program.data.repository.ExerciseRepositoryImpl
import com.example.fitnesslog.program.domain.repository.ExerciseRepository
import com.example.fitnesslog.program.domain.use_case.exercise_template.AddExercisesToWorkoutTemplate
import com.example.fitnesslog.program.domain.use_case.exercise_template.CreateExerciseTemplate
import com.example.fitnesslog.program.domain.use_case.exercise_template.DeleteExerciseFromWorkoutTemplate
import com.example.fitnesslog.program.domain.use_case.exercise_template.EditExerciseTemplate
import com.example.fitnesslog.program.domain.use_case.exercise_template.ExerciseTemplateUseCases
import com.example.fitnesslog.program.domain.use_case.exercise_template.GetExerciseTemplates
import com.example.fitnesslog.program.domain.use_case.exercise_template.GetExercisesForWorkoutTemplate
import com.example.fitnesslog.program.domain.use_case.exercise_template.ReorderExercisesForWorkoutTemplate
import com.example.fitnesslog.program.domain.use_case.exercise_template.TryDeleteExerciseTemplate

interface ExerciseTemplateModule {
    val exerciseTemplateUseCases: ExerciseTemplateUseCases
    val exerciseTemplateDao: ExerciseTemplateDao
    val exerciseRepository: ExerciseRepository
}

class ExerciseTemplateModuleImpl(private val db: FitnessLogDatabase) : ExerciseTemplateModule {
    override val exerciseTemplateDao: ExerciseTemplateDao by lazy {
        db.exerciseTemplateDao()
    }
    override val exerciseRepository: ExerciseRepository by lazy {
        ExerciseRepositoryImpl(exerciseTemplateDao)
    }

    override val exerciseTemplateUseCases: ExerciseTemplateUseCases by lazy {
        ExerciseTemplateUseCases(
            createExerciseTemplate = CreateExerciseTemplate(exerciseRepository),
            getExerciseTemplates = GetExerciseTemplates(exerciseRepository),
            editExerciseTemplate = EditExerciseTemplate(exerciseRepository),
            tryDeleteExerciseTemplate = TryDeleteExerciseTemplate(exerciseRepository),
            addExercisesToWorkoutTemplate = AddExercisesToWorkoutTemplate(exerciseRepository),
            getExercisesForWorkoutTemplate = GetExercisesForWorkoutTemplate(exerciseRepository),
            reorderExercisesForWorkoutTemplate = ReorderExercisesForWorkoutTemplate(
                exerciseRepository
            ),
            deleteExerciseFromWorkoutTemplate = DeleteExerciseFromWorkoutTemplate(exerciseRepository)
        )
    }
}