package com.example.fitnesslog.shared.di

import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.FitnessLogApp.Companion.workoutModule
import com.example.fitnesslog.core.data.database.FitnessLogDatabase
import com.example.fitnesslog.shared.data.repository.SharedRepositoryImpl
import com.example.fitnesslog.shared.domain.repository.SharedRepository
import com.example.fitnesslog.shared.domain.use_case.GetSelectedProgram
import com.example.fitnesslog.shared.domain.use_case.SeedInitialApplication
import com.example.fitnesslog.shared.domain.use_case.SharedUseCases

interface SharedModule {
    val sharedRepository: SharedRepository
    val sharedUseCases: SharedUseCases
}

class SharedModuleImpl(private val db: FitnessLogDatabase) : SharedModule {

    override val sharedRepository: SharedRepository by lazy {
        SharedRepositoryImpl(
            db,
            programModule.programDao,
            workoutModule.workoutTemplateDao,
            appModule.dataStore
        )
    }

    override val sharedUseCases: SharedUseCases by lazy {
        SharedUseCases(
            getSelectedProgram = GetSelectedProgram(programModule.programRepository),
            seedInitialApplication = SeedInitialApplication(sharedRepository)
        )
    }

}