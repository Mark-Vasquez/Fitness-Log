package com.example.fitnesslog.domain.use_case.workout_template

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplate
import com.example.fitnesslog.domain.repository.TemplateRepository
import kotlinx.coroutines.flow.Flow

class GetWorkoutTemplates(
    private val templateRepository: TemplateRepository
) {
    operator fun invoke(programId: Int): Flow<Resource<List<WorkoutTemplate>>> {
        return templateRepository.getWorkoutTemplatesForProgramOrderedByPosition(programId)
    }
}