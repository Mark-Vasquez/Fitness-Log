package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource

class DeleteWorkoutTemplate(
    private val templateRepository: com.example.fitnesslog.domain.repository.TemplateRepository
) {
    suspend operator fun invoke(workoutTemplateId: Int, programId: Int): Resource<Unit> {
        return templateRepository.deleteWorkoutTemplateAndRearrange(
            workoutTemplateId,
            programId
        )
    }
}