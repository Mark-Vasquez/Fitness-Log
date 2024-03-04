package com.example.fitnesslog.domain.use_case.program

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.repository.ProgramRepository

class DeleteProgram(
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke(programId: Int): Resource<Unit> {
        return programRepository.deleteProgram(programId)
    }
}