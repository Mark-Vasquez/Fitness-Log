package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.repository.TemplateRepository

class DeleteExerciseTemplate(private val templateRepository: TemplateRepository) {
    suspend operator fun invoke(exerciseTemplateId: Int): Resource<Unit> {
        return templateRepository.deleteExerciseTemplate(exerciseTemplateId)
    }
}