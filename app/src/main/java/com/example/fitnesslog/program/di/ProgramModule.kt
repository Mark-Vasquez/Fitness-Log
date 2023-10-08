package com.example.fitnesslog.program.di

import com.example.fitnesslog.core.data.database.FitnessLogDatabase
import com.example.fitnesslog.program.data.repository.ProgramRepositoryImpl
import com.example.fitnesslog.program.domain.repository.ProgramRepository
import com.example.fitnesslog.program.domain.use_case.CreateProgramUseCase
import com.example.fitnesslog.program.domain.use_case.DeleteProgramUseCase
import com.example.fitnesslog.program.domain.use_case.EditProgramUseCase
import com.example.fitnesslog.program.domain.use_case.GetProgramsUseCase
import com.example.fitnesslog.program.domain.use_case.ProgramUseCases
import com.example.fitnesslog.program.domain.use_case.SelectProgramUseCase

interface ProgramModule {
    val programRepository: ProgramRepository
    val programUseCases: ProgramUseCases
}

class ProgramModuleImpl(
    private val db: FitnessLogDatabase
) : ProgramModule {
    override val programRepository: ProgramRepository by lazy {
        ProgramRepositoryImpl(db.programDao())
    }

    override val programUseCases: ProgramUseCases by lazy {
        ProgramUseCases(
            createProgramUseCase = CreateProgramUseCase(programRepository),
            getProgramUseCase = GetProgramsUseCase(programRepository),
            editProgramUseCase = EditProgramUseCase(programRepository),
            deleteProgramUseCase = DeleteProgramUseCase(programRepository),
            selectProgramUseCase = SelectProgramUseCase(programRepository)
        )
    }
}