package com.example.fitnesslog.workout.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.workout.data.entity.WorkoutTemplate
import com.example.fitnesslog.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class GetWorkoutTemplates(
    private val workoutRepository: WorkoutRepository
) {
    operator fun invoke(programId: Int): Flow<Resource<List<WorkoutTemplate>>> {
        return workoutRepository.getWorkoutTemplatesForProgramOrderedByPosition(programId)
    }
}