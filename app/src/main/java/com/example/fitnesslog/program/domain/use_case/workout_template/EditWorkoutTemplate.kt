package com.example.fitnesslog.program.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.program.domain.repository.WorkoutRepository

class EditWorkoutTemplate(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(workoutTemplate: WorkoutTemplate): Resource<Int> {
        return workoutRepository.updateWorkoutTemplate(workoutTemplate)
    }
}