package com.example.fitnesslog.program.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.repository.ProgramRepository

class GetProgram(private val programRepository: ProgramRepository) {
    suspend operator fun invoke(programId: Int): Resource<Program> {
        return programRepository.getProgramById(programId)
    }
}