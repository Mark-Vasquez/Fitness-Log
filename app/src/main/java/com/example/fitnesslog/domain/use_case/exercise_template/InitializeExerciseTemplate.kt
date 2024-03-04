package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.repository.TemplateRepository

class InitializeExerciseTemplate(
    private val templateRepository: TemplateRepository
) {
    suspend operator fun invoke(): Resource<Long> {
        return templateRepository.initializeExerciseTemplate()
    }
}