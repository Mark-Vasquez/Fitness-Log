package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplate
import com.example.fitnesslog.domain.repository.TemplateRepository

class ReorderWorkoutTemplates(
    private val templateRepository: TemplateRepository
) {
    suspend operator fun invoke(
        workoutTemplates: List<WorkoutTemplate>
    ): Resource<Unit> {
        return templateRepository.updateWorkoutTemplatePositionsForProgram(
            workoutTemplates
        )
    }
}