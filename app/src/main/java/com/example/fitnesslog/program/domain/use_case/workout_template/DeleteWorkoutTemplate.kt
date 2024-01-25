package com.example.fitnesslog.program.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.repository.WorkoutRepository

class DeleteWorkoutTemplate(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workoutTemplateId: Int, programId: Int): Resource<Unit> {
        return workoutRepository.deleteWorkoutTemplateAndRearrange(workoutTemplateId, programId)
    }
}