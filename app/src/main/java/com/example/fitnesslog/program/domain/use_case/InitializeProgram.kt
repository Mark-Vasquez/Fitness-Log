package com.example.fitnesslog.program.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.repository.ProgramRepository

// Creating a pre-populated program when clicking "Create", so we have ID to set workouts to
class InitializeProgram(
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke(): Resource<Long> {
        val defaultProgram = Program(
            name = "",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        return programRepository.insertProgram(defaultProgram)
    }
}