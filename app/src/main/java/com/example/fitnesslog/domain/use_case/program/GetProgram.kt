package com.example.fitnesslog.domain.use_case.program

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.Program
import com.example.fitnesslog.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow

class GetProgram(private val programRepository: ProgramRepository) {
    suspend operator fun invoke(programId: Int): Flow<Resource<Program>> {
        return programRepository.getProgramById(programId)
    }
}