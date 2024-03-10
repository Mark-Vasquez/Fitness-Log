package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.repository.TemplateRepository

class DeleteExerciseTemplates(private val templateRepository: TemplateRepository) {
    suspend operator fun invoke(exerciseTemplateIds: List<Int>): Resource<Unit> {
        return templateRepository.deleteExerciseTemplates(exerciseTemplateIds)
    }
}