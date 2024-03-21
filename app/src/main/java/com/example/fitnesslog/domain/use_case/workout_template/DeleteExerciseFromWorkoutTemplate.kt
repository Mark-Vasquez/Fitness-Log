package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.repository.TemplateRepository

class DeleteExerciseFromWorkoutTemplate(private val templateRepository: TemplateRepository) {
    suspend operator fun invoke(
        workoutTemplateExerciseId: Int,
        workoutTemplateId: Int
    ): Resource<Unit> {
        return templateRepository.deleteExerciseInWorkoutTemplateAndRearrange(
            workoutTemplateExerciseId,
            workoutTemplateId
        )
    }
}
