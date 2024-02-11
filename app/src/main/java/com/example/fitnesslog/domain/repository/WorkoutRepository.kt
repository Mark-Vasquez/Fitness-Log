package com.example.fitnesslog.domain.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun insertWorkoutTemplate(workoutTemplate: WorkoutTemplate): Resource<Long>

    suspend fun getPositionForInsert(programId: Int): Resource<Int>
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
}