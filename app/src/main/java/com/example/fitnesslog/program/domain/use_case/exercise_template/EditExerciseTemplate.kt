package com.example.fitnesslog.program.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.ExerciseTemplate
import com.example.fitnesslog.program.domain.repository.ExerciseRepository

class EditExerciseTemplate(private val exerciseRepository: ExerciseRepository) {
    suspend operator fun invoke(exerciseTemplate: ExerciseTemplate): Resource<Int> {
        return exerciseRepository.updateExerciseTemplate(exerciseTemplate)
    }
}
