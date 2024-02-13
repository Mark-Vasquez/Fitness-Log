package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.ExerciseTemplate
import com.example.fitnesslog.domain.repository.TemplateRepository

class EditExerciseTemplate(private val templateRepository: TemplateRepository) {
    suspend operator fun invoke(exerciseTemplate: ExerciseTemplate): Resource<Int> {
        return templateRepository.updateExerciseTemplate(exerciseTemplate)
    }
}
