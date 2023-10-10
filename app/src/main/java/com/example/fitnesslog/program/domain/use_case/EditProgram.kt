package com.example.fitnesslog.program.domain.use_case

import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.repository.ProgramRepository

class EditProgram(
    private val programRepository: ProgramRepository
) {

    suspend operator fun invoke(program: Program): Int {
        return programRepository.updateProgram(program)
    }
}