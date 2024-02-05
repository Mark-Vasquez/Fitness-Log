package com.example.fitnesslog.program.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.repository.ExerciseRepository

class DeleteExerciseFromWorkoutTemplate(private val exerciseRepository: ExerciseRepository) {
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
