package com.example.fitnesslog.shared.di

import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.shared.domain.use_case.GetSelectedProgram
import com.example.fitnesslog.shared.domain.use_case.SharedUseCases

interface SharedModule {
    val sharedUseCases: SharedUseCases
}

class SharedModuleImpl() : SharedModule {

    override val sharedUseCases: SharedUseCases by lazy {
        SharedUseCases(
            getSelectedProgram = GetSelectedProgram(programModule.programRepository)
        )
    }

}