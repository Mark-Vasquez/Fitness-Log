package com.example.fitnesslog

import android.app.Application
import com.example.fitnesslog.core.di.AppModule
import com.example.fitnesslog.core.di.AppModuleImpl
import com.example.fitnesslog.program.di.ProgramModule
import com.example.fitnesslog.program.di.ProgramModuleImpl
import com.example.fitnesslog.program.di.WorkoutModule
import com.example.fitnesslog.program.di.WorkoutModuleImpl
import com.example.fitnesslog.shared.di.SharedModule
import com.example.fitnesslog.shared.di.SharedModuleImpl

// Manual dependency injection for each feature Module
// Each module implementation instance also provides dependencies that can be injected throughout app
class FitnessLogApp : Application() {
    companion object {
        lateinit var appModule: AppModule
        lateinit var programModule: ProgramModule
        lateinit var workoutModule: WorkoutModule
        lateinit var sharedModule: SharedModule
    }

    /**
     * This pattern allows for easy implementation changes while sticking to the interface contract
     * For example, the 'appModule' instance can easily be swapped for 'AppModuleTestingImpl',
     * as long as it conforms to the 'AppModule' interface, allowing easier testing or configuration changes
     */
    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
        programModule = ProgramModuleImpl(appModule.db)
        workoutModule = WorkoutModuleImpl(appModule.db)
        sharedModule = SharedModuleImpl(appModule.db)

    }
}