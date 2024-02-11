package com.example.fitnesslog.domain.use_case.program

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.Program

class EditProgram(
    private val programRepository: com.example.fitnesslog.domain.repository.ProgramRepository
) {

    suspend operator fun invoke(program: Program): Resource<Unit> {
        return programRepository.updateAndSelectProgram(program)
    }
}