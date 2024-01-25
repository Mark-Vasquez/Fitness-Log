package com.example.fitnesslog.program.domain.use_case.program

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.repository.ProgramRepository

class EditProgram(
    private val programRepository: ProgramRepository
) {

    suspend operator fun invoke(program: Program): Resource<Unit> {
        return programRepository.updateAndSelectProgram(program)
    }
}