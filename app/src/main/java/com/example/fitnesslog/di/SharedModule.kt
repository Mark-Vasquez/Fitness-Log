package com.example.fitnesslog.di

import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.FitnessLogApp.Companion.exerciseTemplateModule
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.FitnessLogApp.Companion.workoutTemplateModule
import com.example.fitnesslog.core.database.FitnessLogDatabase
import com.example.fitnesslog.data.repository.SharedRepositoryImpl
import com.example.fitnesslog.domain.repository.SharedRepository
import com.example.fitnesslog.domain.use_case.shared.GetSelectedProgram
import com.example.fitnesslog.domain.use_case.shared.SeedInitialApplication
import com.example.fitnesslog.domain.use_case.shared.SharedUseCases

interface SharedModule {
    val sharedRepository: SharedRepository
    val sharedUseCases: SharedUseCases
}

class SharedModuleImpl(private val db: FitnessLogDatabase) : SharedModule {

    override val sharedRepository: SharedRepository by lazy {
        SharedRepositoryImpl(
            db,
            programModule.programDao,
            workoutTemplateModule.workoutTemplateDao,
            exerciseTemplateModule.exerciseTemplateDao,
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