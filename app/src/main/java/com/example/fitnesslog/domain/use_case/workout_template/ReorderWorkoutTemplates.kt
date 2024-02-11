package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplate

class ReorderWorkoutTemplates(
    private val workoutRepository: com.example.fitnesslog.domain.repository.WorkoutRepository
) {
    suspend operator fun invoke(
        workoutTemplates: List<WorkoutTemplate>
    ): Resource<Unit> {
        return workoutRepository.updateWorkoutTemplatePositionsForProgram(
            workoutTemplates
        )
    }
}