package com.example.fitnesslog

import android.app.Application
import com.example.fitnesslog.di.AppModule
import com.example.fitnesslog.di.AppModuleImpl
import com.example.fitnesslog.di.ExerciseTemplateModule
import com.example.fitnesslog.di.ExerciseTemplateModuleImpl
import com.example.fitnesslog.di.ProgramModule
import com.example.fitnesslog.di.ProgramModuleImpl
import com.example.fitnesslog.di.SharedModule
import com.example.fitnesslog.di.SharedModuleImpl
import com.example.fitnesslog.di.WorkoutTemplateModule
import com.example.fitnesslog.di.WorkoutTemplateModuleImpl

// Manual dependency injection for each feature Module
// Each module implementation instance also provides dependencies that can be injected throughout app
class FitnessLogApp : Application() {
    companion object {
        lateinit var appModule: AppModule
        lateinit var programModule: ProgramModule
        lateinit var workoutTemplateModule: WorkoutTemplateModule
        lateinit var exerciseTemplateModule: ExerciseTemplateModule
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
        workoutTemplateModule = WorkoutTemplateModuleImpl(appModule.db)
        exerciseTemplateModule = ExerciseTemplateModuleImpl(appModule.db)
        sharedModule = SharedModuleImpl(appModule.db)

    }
}