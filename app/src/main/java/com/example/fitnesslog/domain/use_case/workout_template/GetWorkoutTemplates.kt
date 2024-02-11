package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

class GetWorkoutTemplates(
    private val workoutRepository: com.example.fitnesslog.domain.repository.WorkoutRepository
) {
    operator fun invoke(programId: Int): Flow<Resource<List<WorkoutTemplate>>> {
        return workoutRepository.getWorkoutTemplatesForProgramOrderedByPosition(programId)
    }
}