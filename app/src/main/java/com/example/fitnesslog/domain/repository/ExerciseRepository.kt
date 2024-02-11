package com.example.fitnesslog.domain.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.ExerciseTemplate
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    // **Exercise Template**
    suspend fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Long>

    // get exercise template to edit?

    fun getAllExercisesOrderedByName(): Flow<Resource<List<ExerciseTemplate>>>

    suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Int>

    suspend fun tryDeleteExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Boolean>


    // **Exercises for Specific Workout Template**
    suspend fun addExercisesToWorkoutTemplate(
        exerciseTemplateIds: List<Int>,
        workoutTemplateId: Int
    ): Resource<LongArray>

    fun getWorkoutTemplateExercisesOrderedByPosition(workoutTemplateId: Int): Flow<Resource<List<com.example.fitnesslog.domain.model.WorkoutTemplateExerciseWithName>>>

    suspend fun updateAllExercisePositionsForWorkoutTemplate(workoutExercises: List<com.example.fitnesslog.domain.model.WorkoutTemplateExerciseWithName>): Resource<Unit>

    suspend fun deleteExerciseInWorkoutTemplateAndRearrange(
        exerciseTemplateId: Int,
        workoutTemplateId: Int
    ): Resource<Unit>
}