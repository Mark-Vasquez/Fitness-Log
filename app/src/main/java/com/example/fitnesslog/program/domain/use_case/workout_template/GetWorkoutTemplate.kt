package com.example.fitnesslog.program.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.program.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow

class GetWorkoutTemplate(private val workoutRepository: WorkoutRepository) {
    suspend operator fun invoke(workoutTemplateId: Int): Flow<Resource<WorkoutTemplate>> {
        return workoutRepository.getWorkoutTemplateById(workoutTemplateId)
    }
}