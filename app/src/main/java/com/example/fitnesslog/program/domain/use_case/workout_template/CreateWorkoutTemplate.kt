package com.example.fitnesslog.program.domain.use_case.workout_template

import android.util.Log
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.program.domain.repository.WorkoutRepository

class CreateWorkoutTemplate(
    private val workoutRepository: WorkoutRepository
) {
    suspend operator fun invoke(
        programId: Int
    ): Resource<Long> {
        when (val resource = workoutRepository.getPositionForInsert(programId)) {
            is Resource.Success -> {
                val lastPosition = resource.data
                val workoutTemplate = WorkoutTemplate(
                    name = "New Workout",
                    programId = programId,
                    position = lastPosition,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                Log.d("Debugga", "${resource.data} $workoutTemplate")
                return workoutRepository.insertWorkoutTemplate(workoutTemplate)
            }

            else -> {
                Log.d("Debugga", "${resource.data} ")
                return Resource.Error("Failed to get the last position ${resource.errorMessage}")
            }
        }
    }
}