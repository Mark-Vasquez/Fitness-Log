package com.example.fitnesslog

import android.app.Application
import com.example.fitnesslog.core.di.AppModule
import com.example.fitnesslog.core.di.AppModuleImpl
import com.example.fitnesslog.program.di.ProgramModule
import com.example.fitnesslog.program.di.ProgramModuleImpl

class FitnessLogApp : Application() {
    companion object {
        lateinit var appModule: AppModule
        lateinit var programModule: ProgramModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
        programModule = ProgramModuleImpl(appModule.db)
    }
}