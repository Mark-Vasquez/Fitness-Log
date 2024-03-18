package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.repository.TemplateRepository

class DeleteWorkoutTemplate(
    private val templateRepository: TemplateRepository
) {
    suspend operator fun invoke(workoutTemplateId: Int, programId: Int): Resource<Unit> {
        return templateRepository.deleteWorkoutTemplateAndRearrange(
            workoutTemplateId,
            programId
        )
    }
}