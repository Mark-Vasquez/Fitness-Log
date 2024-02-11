package com.example.fitnesslog.domain.use_case.program

import com.example.fitnesslog.core.utils.Resource

class DeleteProgram(
    private val programRepository: com.example.fitnesslog.domain.repository.ProgramRepository
) {
    suspend operator fun invoke(programId: Int): Resource<Unit> {
        return programRepository.deleteProgram(programId)
    }
}