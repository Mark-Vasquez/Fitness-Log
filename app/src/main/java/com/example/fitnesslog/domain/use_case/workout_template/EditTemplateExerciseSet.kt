package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplateExerciseSet
import com.example.fitnesslog.domain.repository.TemplateRepository

class EditTemplateExerciseSet(private val templateRepository: TemplateRepository) {
    suspend operator fun invoke(workoutTemplateExerciseSet: WorkoutTemplateExerciseSet): Resource<Int> {
        return templateRepository.updateWorkoutTemplateExerciseSet(workoutTemplateExerciseSet)
    }
}