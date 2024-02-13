package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.ExerciseTemplate
import com.example.fitnesslog.domain.repository.TemplateRepository

class CreateExerciseTemplate(private val templateRepository: TemplateRepository) {
    suspend operator fun invoke(exerciseTemplate: ExerciseTemplate): Resource<Long> {
        return templateRepository.insertExerciseTemplate(exerciseTemplate)
    }
}
