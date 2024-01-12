package com.example.fitnesslog.workout.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.workout.data.entity.WorkoutTemplate
import com.example.fitnesslog.workout.domain.repository.WorkoutRepository

class ReorderWorkoutTemplate(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(
        workoutTemplates: List<WorkoutTemplate>,
        programId: Int
    ): Resource<Unit> {
        return workoutRepository.updateWorkoutTemplatePositionsForProgram(
            workoutTemplates,
            programId
        )
    }
}