package com.example.fitnesslog.workout.domain.repository

import com.example.fitnesslog.workout.data.entity.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun insertWorkoutTemplate(workoutTemplate: WorkoutTemplate): Long

    suspend fun getPositionForInsert(programId: Int): Int

    fun getWorkoutTemplatesForProgramOrderedByPosition(programId: Int): Flow<List<WorkoutTemplate>>

    suspend fun updateWorkoutTemplate(workoutTemplate: WorkoutTemplate): Int

    suspend fun updateWorkoutTemplatePositionsForProgram(
        workoutTemplates: List<WorkoutTemplate>,
        programId: Int
    )

    suspend fun deleteWorkoutTemplateAndRearrange(workoutTemplate: WorkoutTemplate)
}