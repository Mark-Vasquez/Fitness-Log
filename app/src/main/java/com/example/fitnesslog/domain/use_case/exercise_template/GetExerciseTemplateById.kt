package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.ExerciseTemplate
import com.example.fitnesslog.domain.repository.TemplateRepository
import kotlinx.coroutines.flow.Flow

class GetExerciseTemplateById(private val templateRepository: TemplateRepository) {
    operator fun invoke(exerciseTemplateId: Int): Flow<Resource<ExerciseTemplate>> {
        return templateRepository.getExerciseTemplateById(exerciseTemplateId)
    }
}