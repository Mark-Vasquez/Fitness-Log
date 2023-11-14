package com.example.fitnesslog.program.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.repository.ProgramRepository

class SeedProgram(
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return programRepository.seedDatabaseIfFirstRun()
    }
}