package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.repository.TemplateRepository

class DeleteSetFromWorkoutTemplateExercise(
    private val templateRepository: TemplateRepository
) {
    suspend operator fun invoke(
        workoutTemplateExerciseSetId: Int,
        workoutTemplateExerciseId: Int
    ): Resource<Unit> {
        return templateRepository.deleteWorkoutTemplateExerciseSetAndRearrange(
            workoutTemplateExerciseSetId,
            workoutTemplateExerciseId
        )
    }
}