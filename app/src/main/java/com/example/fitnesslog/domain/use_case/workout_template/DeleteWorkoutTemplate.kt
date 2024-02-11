package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource

class DeleteWorkoutTemplate(
    private val workoutRepository: com.example.fitnesslog.domain.repository.WorkoutRepository
) {
    suspend operator fun invoke(workoutTemplateId: Int, programId: Int): Resource<Unit> {
        return workoutRepository.deleteWorkoutTemplateAndRearrange(workoutTemplateId, programId)
    }
}