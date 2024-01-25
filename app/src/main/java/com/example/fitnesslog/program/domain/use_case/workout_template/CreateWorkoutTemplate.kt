package com.example.fitnesslog.program.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.program.domain.repository.WorkoutRepository

class CreateWorkoutTemplate(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(
        name: String,
        programId: Int
    ): Resource<Long> {
        return when (val lastPosition = workoutRepository.getPositionForInsert(programId)) {
            is Resource.Success -> {
                val workoutTemplate = WorkoutTemplate(
                    name = name,
                    programId = programId,
                    position = lastPosition.data,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                workoutRepository.insertWorkoutTemplate(workoutTemplate)
            }

            else -> {
                Resource.Error("Failed to get the last position ${lastPosition.errorMessage}")
            }
        }
    }
}