package com.example.fitnesslog.program.data.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.core.utils.extensions.toErrorMessage
import com.example.fitnesslog.core.utils.safeCall
import com.example.fitnesslog.program.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.program.data.entity.ExerciseTemplate
import com.example.fitnesslog.program.domain.model.WorkoutTemplateExerciseWithName
import com.example.fitnesslog.program.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class ExerciseRepositoryImpl(private val dao: ExerciseTemplateDao) : ExerciseRepository {
    override suspend fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Long> {
        return safeCall { dao.insertExerciseTemplate(exerciseTemplate) }
    }

    override fun getAllExercisesOrderedByName(): Flow<Resource<List<ExerciseTemplate>>> {
        return dao.getAllExercisesOrderedByName()
            .map { Resource.Success(it) as Resource<List<ExerciseTemplate>> }
            .catch { e -> emit(Resource.Error(e.toErrorMessage())) }
    }

    override suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Int> {
        return safeCall { dao.updateExerciseTemplate(exerciseTemplate) }
    }

    override suspend fun tryDeleteExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Boolean> {
        return safeCall { dao.tryDeleteExerciseTemplate(exerciseTemplate) }
    }

    override suspend fun addExercisesToWorkoutTemplate(
        exerciseTemplateIds: List<Int>,
        workoutTemplateId: Int
    ): Resource<LongArray> {
        return safeCall {
            dao.addExercisesToWorkoutTemplate(
                exerciseTemplateIds,
                workoutTemplateId
            )
        }
    }

    override fun getWorkoutTemplateExercisesOrderedByPosition(workoutTemplateId: Int): Flow<Resource<List<WorkoutTemplateExerciseWithName>>> {
        return dao.getWorkoutTemplateExercisesOrderedByPosition(workoutTemplateId)
            .map { Resource.Success(it) as Resource<List<WorkoutTemplateExerciseWithName>> }
            .catch { e -> emit(Resource.Error(e.toErrorMessage())) }
    }

    override suspend fun updateAllExercisePositionsForWorkoutTemplate(workoutExercises: List<WorkoutTemplateExerciseWithName>): Resource<Unit> {
        return safeCall { dao.updateAllExercisePositionsForWorkoutTemplate(workoutExercises) }
    }

    override suspend fun deleteExerciseInWorkoutTemplateAndRearrange(
        exerciseTemplateId: Int,
        workoutTemplateId: Int
    ): Resource<Unit> {
        return safeCall {
            dao.deleteExerciseInWorkoutTemplateAndRearrange(
                exerciseTemplateId,
                workoutTemplateId
            )
        }
    }
}