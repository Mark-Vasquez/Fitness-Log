package com.example.fitnesslog

import android.app.Application
import com.example.fitnesslog.di.AppModule
import com.example.fitnesslog.di.AppModuleImpl

// Manual dependency injection for each feature Module
// Each module implementation instance also provides dependencies that can be injected throughout app
class FitnessLogApp : Application() {
    companion object {
        lateinit var appModule: AppModule
    }

    /**
     * This pattern allows for easy implementation changes while sticking to the interface contract
     * For example, the 'appModule' instance can easily be swapped for 'AppModuleTestingImpl',
     * as long as it uses the 'AppModule' interface, allowing easier testing or configuration changes (swapping)
     */
    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)

    }
}