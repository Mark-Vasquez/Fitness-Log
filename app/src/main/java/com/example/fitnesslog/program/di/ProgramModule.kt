package com.example.fitnesslog.program.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.fitnesslog.core.data.database.FitnessLogDatabase
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.data.repository.ProgramRepositoryImpl
import com.example.fitnesslog.program.domain.repository.ProgramRepository
import com.example.fitnesslog.program.domain.use_case.DeleteProgram
import com.example.fitnesslog.program.domain.use_case.EditProgram
import com.example.fitnesslog.program.domain.use_case.GetPrograms
import com.example.fitnesslog.program.domain.use_case.InitializeProgram
import com.example.fitnesslog.program.domain.use_case.ProgramUseCases
import com.example.fitnesslog.program.domain.use_case.SeedProgram
import com.example.fitnesslog.program.domain.use_case.SelectProgram

interface ProgramModule {
    val programDao: ProgramDao
    val programRepository: ProgramRepository
    val programUseCases: ProgramUseCases
}

// Instantiates and exposes the singletons that can be used from instantiating ProgramModuleImpl and using that
// programModule to access the interface properties
class ProgramModuleImpl(
    private val db: FitnessLogDatabase,
    private val dataStore: DataStore<Preferences>
) : ProgramModule {
    override val programDao: ProgramDao by lazy {
        db.programDao()
    }

    override val programRepository: ProgramRepository by lazy {
        ProgramRepositoryImpl(programDao, dataStore)
    }

    override val programUseCases: ProgramUseCases by lazy {
        ProgramUseCases(
            initializeProgram = InitializeProgram(programRepository),
            getPrograms = GetPrograms(programRepository),
            editProgram = EditProgram(programRepository),
            deleteProgram = DeleteProgram(programRepository),
            selectProgram = SelectProgram(programRepository),
            seedProgram = SeedProgram(programRepository)
        )
    }
}