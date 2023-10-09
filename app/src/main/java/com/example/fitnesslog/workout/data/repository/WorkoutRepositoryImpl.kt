package com.example.fitnesslog.workout.data.repository

import com.example.fitnesslog.workout.data.dao.WorkoutDao
import com.example.fitnesslog.workout.data.entity.WorkoutTemplate
import com.example.fitnesslog.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class WorkoutRepositoryImpl(private val dao: WorkoutDao) : WorkoutRepository {
    override suspend fun insertWorkoutTemplate(workoutTemplate: WorkoutTemplate): Long {
        return dao.insertWorkoutTemplate(workoutTemplate)
    }

    override suspend fun getPositionForInsert(programId: Int): Int {
        return dao.getPositionForInsert(programId)
    }

    override fun getWorkoutTemplatesForProgramOrderedByPosition(programId: Int): Flow<List<WorkoutTemplate>> {
        return dao.getWorkoutTemplatesForProgramOrderedByPosition(programId)
    }

    override suspend fun updateWorkoutTemplate(workoutTemplate: WorkoutTemplate): Int {
        return dao.updateWorkoutTemplate(workoutTemplate)
    }

    override suspend fun updateWorkoutTemplatePositionsForProgram(
        workoutTemplates: List<WorkoutTemplate>,
        programId: Int
    ) {
        return dao.updateWorkoutTemplatePositionsForProgram(workoutTemplates, programId)
    }

    override suspend fun deleteWorkoutTemplateAndRearrange(workoutTemplate: WorkoutTemplate) {
        return dao.deleteWorkoutTemplateAndRearrange(workoutTemplate)
    }
}