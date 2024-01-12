package com.example.fitnesslog.workout.data.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.core.utils.safeCall
import com.example.fitnesslog.core.utils.toErrorMessage
import com.example.fitnesslog.workout.data.dao.WorkoutDao
import com.example.fitnesslog.workout.data.entity.WorkoutTemplate
import com.example.fitnesslog.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class WorkoutRepositoryImpl(private val dao: WorkoutDao) : WorkoutRepository {
    override suspend fun insertWorkoutTemplate(workoutTemplate: WorkoutTemplate): Resource<Long> {
        return safeCall { dao.insertWorkoutTemplate(workoutTemplate) }
    }

    override suspend fun getPositionForInsert(programId: Int): Resource<Int> {
        return safeCall { dao.getPositionForInsert(programId) }
    }

    override fun getWorkoutTemplatesForProgramOrderedByPosition(programId: Int): Flow<Resource<List<WorkoutTemplate>>> {
        return dao.getWorkoutTemplatesForProgramOrderedByPosition(programId)
            .map { Resource.Success(it) as Resource<List<WorkoutTemplate>> }
            .catch { e ->
                emit(
                    Resource.Error(e.toErrorMessage())
                )
            }
    }

    override suspend fun updateWorkoutTemplate(workoutTemplate: WorkoutTemplate): Resource<Int> {
        return safeCall { dao.updateWorkoutTemplate(workoutTemplate) }
    }

    override suspend fun updateWorkoutTemplatePositionsForProgram(
        workoutTemplates: List<WorkoutTemplate>,
        programId: Int
    ): Resource<Unit> {
        return safeCall {
            dao.updateWorkoutTemplatePositionsForProgram(
                workoutTemplates,
                programId
            )
        }
    }

    override suspend fun deleteWorkoutTemplateAndRearrange(workoutTemplate: WorkoutTemplate): Resource<Unit> {
        return safeCall { dao.deleteWorkoutTemplateAndRearrange(workoutTemplate) }
    }
}