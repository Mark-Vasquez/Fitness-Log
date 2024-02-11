package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource

class DeleteExerciseFromWorkoutTemplate(private val exerciseRepository: com.example.fitnesslog.domain.repository.ExerciseRepository) {
    suspend operator fun invoke(
        exerciseTemplateId: Int,
        workoutTemplateId: Int
    ): Resource<Unit> {
        return exerciseRepository.deleteExerciseInWorkoutTemplateAndRearrange(
            exerciseTemplateId,
            workoutTemplateId
        )
    }
}
