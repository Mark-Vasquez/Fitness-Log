package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplate

class EditWorkoutTemplate(
    private val templateRepository: com.example.fitnesslog.domain.repository.TemplateRepository
) {
    suspend operator fun invoke(workoutTemplate: WorkoutTemplate): Resource<Int> {
        return templateRepository.updateWorkoutTemplate(workoutTemplate)
    }
}