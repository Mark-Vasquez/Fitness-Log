package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetExercisesForWorkoutTemplate(private val exerciseRepository: com.example.fitnesslog.domain.repository.ExerciseRepository) {
    operator fun invoke(workoutTemplateId: Int): Flow<Resource<List<com.example.fitnesslog.domain.model.WorkoutTemplateExerciseWithName>>> {
        return exerciseRepository.getWorkoutTemplateExercisesOrderedByPosition(workoutTemplateId)
    }
}
