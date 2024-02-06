package com.example.fitnesslog.program.data.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.core.utils.extensions.toErrorMessage
import com.example.fitnesslog.core.utils.safeCall
import com.example.fitnesslog.program.data.dao.WorkoutTemplateDao
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.program.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class WorkoutRepositoryImpl(private val dao: WorkoutTemplateDao) : WorkoutRepository {
    override suspend fun insertWorkoutTemplate(workoutTemplate: WorkoutTemplate): Resource<Long> {
        return safeCall { dao.insertWorkoutTemplate(workoutTemplate) }
    }

    override suspend fun getPositionForInsert(programId: Int): Resource<Int> {
        return safeCall { dao.getPositionForInsert(programId) }
    }

    override fun getWorkoutTemplateById(workoutTemplateId: Int): Flow<Resource<WorkoutTemplate>> {
        return dao.getWorkoutTemplateById(workoutTemplateId)
            .map { Resource.Success(it) as Resource<WorkoutTemplate> }
            .catch { e -> emit(Resource.Error(e.toErrorMessage())) }
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
        workoutTemplates: List<WorkoutTemplate>
    ): Resource<Unit> {
        return safeCall {
            dao.updateAllWorkoutTemplatePositionsForProgram(workoutTemplates)
        }
    }

    override suspend fun deleteWorkoutTemplateAndRearrange(
        workoutTemplateId: Int,
        programId: Int
    ): Resource<Unit> {
        return safeCall {
            dao.deleteWorkoutTemplateInProgramAndRearrange(
                workoutTemplateId,
                programId
            )
        }
    }
}