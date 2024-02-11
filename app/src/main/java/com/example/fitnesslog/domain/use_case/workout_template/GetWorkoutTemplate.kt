package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

class GetWorkoutTemplate(private val workoutRepository: com.example.fitnesslog.domain.repository.WorkoutRepository) {
    suspend operator fun invoke(workoutTemplateId: Int): Flow<Resource<WorkoutTemplate>> {
        return workoutRepository.getWorkoutTemplateById(workoutTemplateId)
    }
}