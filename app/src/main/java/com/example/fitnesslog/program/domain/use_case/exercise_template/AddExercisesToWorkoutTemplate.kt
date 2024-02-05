package com.example.fitnesslog.program.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.repository.ExerciseRepository

class AddExercisesToWorkoutTemplate(
    private val exerciseRepository: ExerciseRepository
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
