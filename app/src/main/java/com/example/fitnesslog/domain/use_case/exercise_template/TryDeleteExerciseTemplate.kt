package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.ExerciseTemplate

class TryDeleteExerciseTemplate(private val exerciseRepository: com.example.fitnesslog.domain.repository.ExerciseRepository) {
    suspend operator fun invoke(exerciseTemplate: ExerciseTemplate): Resource<Boolean> {
        return exerciseRepository.tryDeleteExerciseTemplate(exerciseTemplate)
    }
}
