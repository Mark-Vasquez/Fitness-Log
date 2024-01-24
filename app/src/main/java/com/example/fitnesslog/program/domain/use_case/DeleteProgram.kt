package com.example.fitnesslog.program.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.repository.ProgramRepository

class DeleteProgram(
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke(programId: Int): Resource<Unit> {
        return programRepository.deleteProgram(programId)
    }
}