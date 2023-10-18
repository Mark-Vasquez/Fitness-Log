package com.example.fitnesslog.workout.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.workout.data.entity.WorkoutTemplate
import com.example.fitnesslog.workout.domain.repository.WorkoutRepository

class DeleteWorkoutTemplate(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workoutTemplate: WorkoutTemplate): Resource<Unit> {
        return workoutRepository.deleteWorkoutTemplateAndRearrange(workoutTemplate)
    }
}