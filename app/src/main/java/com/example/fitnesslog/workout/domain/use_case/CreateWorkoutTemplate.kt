package com.example.fitnesslog.workout.domain.use_case

import com.example.fitnesslog.workout.data.entity.WorkoutTemplate
import com.example.fitnesslog.workout.domain.repository.WorkoutRepository

class CreateWorkoutTemplate(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(
        name: String,
        programId: Int
    ): Long {
        val lastPosition = workoutRepository.getPositionForInsert(programId)

        val workoutTemplate = WorkoutTemplate(
            name = name,
            programId = programId,
            position = lastPosition,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        return workoutRepository.insertWorkoutTemplate(workoutTemplate)
    }
}