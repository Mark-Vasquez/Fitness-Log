package com.example.fitnesslog.program.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow

class GetPrograms(
    private val programRepository: ProgramRepository
) {
    operator fun invoke(): Flow<Resource<List<Program>>> {
        return programRepository.getAllProgramsOrderedBySelected()
    }
}