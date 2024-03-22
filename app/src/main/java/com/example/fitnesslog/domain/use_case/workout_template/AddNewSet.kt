package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.repository.TemplateRepository

class AddNewSet(private val templateRepository: TemplateRepository) {
    suspend operator fun invoke(workoutTemplateExerciseId: Int): Resource<Long> {
        return templateRepository.insertNewCopyOfLastSet(workoutTemplateExerciseId)
    }
}