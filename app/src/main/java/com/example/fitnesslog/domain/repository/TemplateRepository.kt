package com.example.fitnesslog.domain.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.ExerciseTemplate
import com.example.fitnesslog.data.entity.WorkoutTemplate
import com.example.fitnesslog.data.entity.WorkoutTemplateExercise
import kotlinx.coroutines.flow.Flow

interface TemplateRepository {
    // **Workout Template**
    suspend fun createWorkoutTemplateForProgram(programId: Int): Resource<Long>

    fun getWorkoutTemplateById(workoutTemplateId: Int): Flow<Resource<WorkoutTemplate>>

    fun getWorkoutTemplatesForProgramOrderedByPosition(programId: Int): Flow<Resource<List<WorkoutTemplate>>>

    suspend fun updateWorkoutTemplate(workoutTemplate: WorkoutTemplate): Resource<Int>

    suspend fun updateWorkoutTemplatePositionsForProgram(
        workoutTemplates: List<WorkoutTemplate>
    ): Resource<Unit>

    suspend fun deleteWorkoutTemplateAndRearrange(
        workoutTemplateId: Int,
        programId: Int
    ): Resource<Unit>

    //**Workout Template Exercise**
    suspend fun addExercisesToWorkoutTemplate(
        exerciseTemplateIds: List<Int>,
        workoutTemplateId: Int
    ): Resource<Unit>


    fun getWorkoutTemplateExercisesOrderedByPosition(workoutTemplateId: Int): Flow<Resource<List<WorkoutTemplateExercise>>>


    suspend fun updateAllExercisePositionsForWorkoutTemplate(workoutTemplateExercises: List<WorkoutTemplateExercise>): Resource<Unit>

    suspend fun deleteExerciseInWorkoutTemplateAndRearrange(
        workoutTemplateExerciseId: Int,
        workoutTemplateId: Int
    ): Resource<Unit>

    // **Workout Template Exercise Set


    // **Exercise Template**
    suspend fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Long>
    suspend fun initializeExerciseTemplate(): Resource<Long>

    suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Int>

    fun getAllExercisesOrderedByName(): Flow<Resource<List<ExerciseTemplate>>>

    fun getExerciseTemplateById(exerciseTemplateId: Int): Flow<Resource<ExerciseTemplate>>

    suspend fun deleteExerciseTemplate(exerciseTemplateId: Int): Resource<Unit>
    suspend fun deleteExerciseTemplates(exerciseTemplateIds: List<Int>): Resource<Unit>
}