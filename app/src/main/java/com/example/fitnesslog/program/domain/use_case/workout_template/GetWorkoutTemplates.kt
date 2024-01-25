package com.example.fitnesslog.program.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.program.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class GetWorkoutTemplates(
    private val workoutRepository: WorkoutRepository
) {
    operator fun invoke(programId: Int): Flow<Resource<List<WorkoutTemplate>>> {
        return workoutRepository.getWorkoutTemplatesForProgramOrderedByPosition(programId)
    }
}