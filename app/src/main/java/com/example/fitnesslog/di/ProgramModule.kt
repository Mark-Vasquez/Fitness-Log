package com.example.fitnesslog.di

import com.example.fitnesslog.core.database.FitnessLogDatabase
import com.example.fitnesslog.data.dao.ProgramDao
import com.example.fitnesslog.data.repository.ProgramRepositoryImpl

interface ProgramModule {
    val programUseCases: com.example.fitnesslog.domain.use_case.program.ProgramUseCases
    val programDao: ProgramDao
    val programRepository: com.example.fitnesslog.domain.repository.ProgramRepository
}

// Instantiates and exposes the singletons that can be used from instantiating ProgramModuleImpl and using that
// programModule to access the interface properties
class ProgramModuleImpl(
    private val db: FitnessLogDatabase,
) : ProgramModule {
    override val programDao: ProgramDao by lazy {
        db.programDao()
    }

    override val programRepository: com.example.fitnesslog.domain.repository.ProgramRepository by lazy {
        ProgramRepositoryImpl(programDao)
    }

    override val programUseCases: com.example.fitnesslog.domain.use_case.program.ProgramUseCases by lazy {
        com.example.fitnesslog.domain.use_case.program.ProgramUseCases(
            initializeProgram = com.example.fitnesslog.domain.use_case.program.InitializeProgram(
                programRepository
            ),
            getPrograms = com.example.fitnesslog.domain.use_case.program.GetPrograms(
                programRepository
            ),
            getProgram = com.example.fitnesslog.domain.use_case.program.GetProgram(programRepository),
            editProgram = com.example.fitnesslog.domain.use_case.program.EditProgram(
                programRepository
            ),
            deleteProgram = com.example.fitnesslog.domain.use_case.program.DeleteProgram(
                programRepository
            ),
            selectProgram = com.example.fitnesslog.domain.use_case.program.SelectProgram(
                programRepository
            ),
            checkIfDeletable = com.example.fitnesslog.domain.use_case.program.CheckIfDeletable(
                programRepository
            )
        )
    }
}