package com.example.fitnesslog

import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.program.domain.repository.ProgramRepository
import com.example.fitnesslog.program.domain.use_case.SelectProgram

interface SharedModule {
    val sharedUseCases: SharedUseCases
}

class SharedModuleImpl() : SharedModule {

    private val programRepository: ProgramRepository = programModule.programRepository

    override val sharedUseCases: SharedUseCases by lazy {
        SharedUseCases(
            selectProgram = SelectProgram(programRepository)
        )
    }

}