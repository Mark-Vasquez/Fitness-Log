package com.example.fitnesslog.program.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.program.domain.repository.WorkoutRepository

class ReorderWorkoutTemplates(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(
        workoutTemplates: List<WorkoutTemplate>
    ): Resource<Unit> {
        return workoutRepository.updateWorkoutTemplatePositionsForProgram(
            workoutTemplates
        )
    }
}