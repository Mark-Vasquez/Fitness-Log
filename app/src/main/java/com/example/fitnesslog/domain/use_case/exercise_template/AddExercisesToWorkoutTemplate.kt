package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource

class AddExercisesToWorkoutTemplate(
    private val exerciseRepository: com.example.fitnesslog.domain.repository.ExerciseRepository
) {
    suspend operator fun invoke(
        exerciseTemplateIds: List<Int>,
        workoutTemplateId: Int
    ): Resource<LongArray> {
        return exerciseRepository.addExercisesToWorkoutTemplate(
            exerciseTemplateIds,
            workoutTemplateId
        )
    }
}
