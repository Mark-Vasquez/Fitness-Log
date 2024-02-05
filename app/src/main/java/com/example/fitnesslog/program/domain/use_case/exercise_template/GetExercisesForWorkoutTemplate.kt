package com.example.fitnesslog.program.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.model.WorkoutTemplateExerciseWithName
import com.example.fitnesslog.program.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class GetExercisesForWorkoutTemplate(private val exerciseRepository: ExerciseRepository) {
    operator fun invoke(workoutTemplateId: Int): Flow<Resource<List<WorkoutTemplateExerciseWithName>>> {
        return exerciseRepository.getWorkoutTemplateExercisesOrderedByPosition(workoutTemplateId)
    }
}
