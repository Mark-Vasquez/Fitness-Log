package com.example.fitnesslog.domain.use_case.shared

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.Program
import kotlinx.coroutines.flow.Flow

class GetSelectedProgram(
    private val programRepository: com.example.fitnesslog.domain.repository.ProgramRepository
) {

    operator fun invoke(): Flow<Resource<Program>> {
        return programRepository.getSelectedProgram()
    }
}