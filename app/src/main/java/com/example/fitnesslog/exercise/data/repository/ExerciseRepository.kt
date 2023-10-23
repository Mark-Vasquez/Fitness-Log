package com.example.fitnesslog.exercise.data.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.exercise.data.entity.ExerciseTemplate
import com.example.fitnesslog.exercise.domain.model.WorkoutTemplateExerciseWithName
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    // **Exercise Template**
    suspend fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Long>

    fun getAllExercisesOrderedByName(): Resource<Flow<List<ExerciseTemplate>>>

    suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Int>

    suspend fun tryDeleteExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Boolean>


    // **Exercises for Specific Workout Template**
    suspend fun addExercisesToWorkoutTemplate(
        exerciseTemplateIds: List<Int>,
        workoutTemplateId: Int
    ): Resource<LongArray>

    fun getExercisesForWorkoutTemplateOrderedByPosition(workoutTemplateId: Int): Resource<Flow<List<WorkoutTemplateExerciseWithName>>>


}