package com.example.fitnesslog.program.di

import com.example.fitnesslog.core.data.database.FitnessLogDatabase
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.data.repository.ProgramRepositoryImpl
import com.example.fitnesslog.program.domain.repository.ProgramRepository
import com.example.fitnesslog.program.domain.use_case.CheckIfDeletable
import com.example.fitnesslog.program.domain.use_case.DeleteProgram
import com.example.fitnesslog.program.domain.use_case.EditProgram
import com.example.fitnesslog.program.domain.use_case.GetProgram
import com.example.fitnesslog.program.domain.use_case.GetPrograms
import com.example.fitnesslog.program.domain.use_case.InitializeProgram
import com.example.fitnesslog.program.domain.use_case.ProgramUseCases
import com.example.fitnesslog.program.domain.use_case.SelectProgram

interface ProgramModule {
    val programUseCases: ProgramUseCases
    val programDao: ProgramDao
    val programRepository: ProgramRepository
}

// Instantiates and exposes the singletons that can be used from instantiating ProgramModuleImpl and using that
// programModule to access the interface properties
class ProgramModuleImpl(
    private val db: FitnessLogDatabase,
) : ProgramModule {
    override val programDao: ProgramDao by lazy {
        db.programDao()
    }

    override val programRepository: ProgramRepository by lazy {
        ProgramRepositoryImpl(programDao)
    }

    override val programUseCases: ProgramUseCases by lazy {
        ProgramUseCases(
            initializeProgram = InitializeProgram(programRepository),
            getPrograms = GetPrograms(programRepository),
            getProgram = GetProgram(programRepository),
            editProgram = EditProgram(programRepository),
            deleteProgram = DeleteProgram(programRepository),
            selectProgram = SelectProgram(programRepository),
            checkIfDeletable = CheckIfDeletable(programRepository)
        )
    }
}