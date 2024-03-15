package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplateExerciseSet
import com.example.fitnesslog.domain.repository.TemplateRepository
import kotlinx.coroutines.flow.Flow

class GetSetsForWorkoutTemplateExerciseId(private val templateRepository: TemplateRepository) {
    operator fun invoke(workoutTemplateExerciseId: Int): Flow<Resource<List<WorkoutTemplateExerciseSet>>> {
        return templateRepository.getWorkoutTemplateExerciseSetsOrderedByPosition(
            workoutTemplateExerciseId
        )
    }
}