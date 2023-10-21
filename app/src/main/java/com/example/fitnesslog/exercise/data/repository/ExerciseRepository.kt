package com.example.fitnesslog.exercise.data.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.exercise.data.entity.ExerciseTemplate
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Long>

    fun getAllExercisesOrderedByName(): Resource<Flow<List<ExerciseTemplate>>>

    suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Int>

    suspend fun tryDeleteExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Boolean>
}