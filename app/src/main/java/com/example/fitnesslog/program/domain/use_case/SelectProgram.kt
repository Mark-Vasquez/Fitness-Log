package com.example.fitnesslog.program.domain.use_case

import com.example.fitnesslog.program.domain.repository.ProgramRepository

class SelectProgram(
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke(programId: Int) {
        programRepository.setProgramAsSelected(programId)
    }
}