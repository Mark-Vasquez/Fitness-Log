package com.example.fitnesslog.program.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.repository.ProgramRepository

class EditProgram(
    private val programRepository: ProgramRepository
) {

    suspend operator fun invoke(program: Program): Resource<Int> {
        return programRepository.updateProgram(program)
    }
}