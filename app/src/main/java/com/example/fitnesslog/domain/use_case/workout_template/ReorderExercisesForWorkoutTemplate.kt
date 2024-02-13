package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplateExercise
import com.example.fitnesslog.domain.repository.TemplateRepository

class ReorderExercisesForWorkoutTemplate(private val templateRepository: TemplateRepository) {
    suspend operator fun invoke(workoutExercises: List<WorkoutTemplateExercise>): Resource<Unit> {
        return templateRepository.updateAllExercisePositionsForWorkoutTemplate(workoutExercises)
    }
}
