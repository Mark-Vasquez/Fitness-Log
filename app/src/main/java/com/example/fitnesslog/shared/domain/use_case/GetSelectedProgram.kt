package com.example.fitnesslog.shared.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow

class GetSelectedProgram(
    private val programRepository: ProgramRepository
) {

    operator fun invoke(): Flow<Resource<Program>> {
        return programRepository.getSelectedProgram()
    }
}