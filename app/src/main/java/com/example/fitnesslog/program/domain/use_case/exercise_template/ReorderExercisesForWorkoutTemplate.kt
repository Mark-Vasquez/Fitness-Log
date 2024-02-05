package com.example.fitnesslog.program.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.model.WorkoutTemplateExerciseWithName
import com.example.fitnesslog.program.domain.repository.ExerciseRepository

class ReorderExercisesForWorkoutTemplate(private val exerciseRepository: ExerciseRepository) {
    suspend operator fun invoke(workoutExercises: List<WorkoutTemplateExerciseWithName>): Resource<Unit> {
        return exerciseRepository.updateAllExercisePositionsForWorkoutTemplate(workoutExercises)
    }
}
