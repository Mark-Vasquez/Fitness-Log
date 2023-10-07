package com.example.fitnesslog

import android.app.Application
import com.example.fitnesslog.core.di.AppModule
import com.example.fitnesslog.core.di.AppModuleImpl

class FitnessLogApp : Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
    }
}