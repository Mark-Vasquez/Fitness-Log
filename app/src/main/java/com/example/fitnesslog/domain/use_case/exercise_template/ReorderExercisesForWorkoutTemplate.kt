package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource

class ReorderExercisesForWorkoutTemplate(private val exerciseRepository: com.example.fitnesslog.domain.repository.ExerciseRepository) {
    suspend operator fun invoke(workoutExercises: List<com.example.fitnesslog.domain.model.WorkoutTemplateExerciseWithName>): Resource<Unit> {
        return exerciseRepository.updateAllExercisePositionsForWorkoutTemplate(workoutExercises)
    }
}
