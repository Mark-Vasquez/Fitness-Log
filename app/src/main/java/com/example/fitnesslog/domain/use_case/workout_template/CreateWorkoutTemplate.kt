package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.repository.TemplateRepository

class CreateWorkoutTemplate(
    private val templateRepository: TemplateRepository
) {
    suspend operator fun invoke(
        programId: Int
    ): Resource<Long> {
        return templateRepository.createWorkoutTemplateForProgram(programId)
    }
}