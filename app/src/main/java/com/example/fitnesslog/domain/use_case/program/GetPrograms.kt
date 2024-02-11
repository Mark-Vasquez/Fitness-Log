package com.example.fitnesslog.domain.use_case.program

import com.example.fitnesslog.core.utils.Resource
import kotlinx.coroutines.flow.Flow

class GetPrograms(
    private val programRepository: com.example.fitnesslog.domain.repository.ProgramRepository
) {
    operator fun invoke(): Flow<Resource<List<com.example.fitnesslog.domain.model.ProgramWithWorkoutCount>>> {
        return programRepository.getAllProgramsOrderedBySelected()
    }
}