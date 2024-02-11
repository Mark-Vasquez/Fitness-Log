package com.example.fitnesslog.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.ExerciseTemplate
import kotlinx.coroutines.flow.Flow

class GetExerciseTemplates(private val exerciseRepository: com.example.fitnesslog.domain.repository.ExerciseRepository) {
    operator fun invoke(): Flow<Resource<List<ExerciseTemplate>>> {
        return exerciseRepository.getAllExercisesOrderedByName()
    }
}
