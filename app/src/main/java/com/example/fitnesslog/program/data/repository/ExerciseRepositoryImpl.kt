package com.example.fitnesslog.program.data.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.program.data.entity.ExerciseTemplate
import com.example.fitnesslog.program.domain.model.WorkoutTemplateExerciseWithName
import com.example.fitnesslog.program.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class ExerciseRepositoryImpl(private val dao: ExerciseTemplateDao) : ExerciseRepository {
    override suspend fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Long> {
        TODO("Not yet implemented")
    }

    override fun getAllExercisesOrderedByName(): Resource<Flow<List<ExerciseTemplate>>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun tryDeleteExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addExercisesToWorkoutTemplate(
        exerciseTemplateIds: List<Int>,
        workoutTemplateId: Int
    ): Resource<LongArray> {
        TODO("Not yet implemented")
    }

    override fun getExercisesForWorkoutTemplateOrderedByPosition(workoutTemplateId: Int): Resource<Flow<List<WorkoutTemplateExerciseWithName>>> {
        TODO("Not yet implemented")
    }
}