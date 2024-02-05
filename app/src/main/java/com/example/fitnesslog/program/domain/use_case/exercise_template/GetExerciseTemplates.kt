package com.example.fitnesslog.program.domain.use_case.exercise_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.ExerciseTemplate
import com.example.fitnesslog.program.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class GetExerciseTemplates(private val exerciseRepository: ExerciseRepository) {
    operator fun invoke(): Flow<Resource<List<ExerciseTemplate>>> {
        return exerciseRepository.getAllExercisesOrderedByName()
    }
}
