package com.example.fitnesslog

import android.app.Application
import com.example.fitnesslog.core.di.AppModule
import com.example.fitnesslog.core.di.AppModuleImpl
import com.example.fitnesslog.program.di.ProgramModule
import com.example.fitnesslog.program.di.ProgramModuleImpl
import com.example.fitnesslog.shared.di.SharedModule
import com.example.fitnesslog.shared.di.SharedModuleImpl
import com.example.fitnesslog.workout.di.WorkoutModule
import com.example.fitnesslog.workout.di.WorkoutModuleImpl

class FitnessLogApp : Application() {
    companion object {
        lateinit var appModule: AppModule
        lateinit var programModule: ProgramModule
        lateinit var workoutModule: WorkoutModule
        lateinit var sharedModule: SharedModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl(this)
        programModule = ProgramModuleImpl(appModule.db)
        workoutModule = WorkoutModuleImpl(appModule.db)
        sharedModule = SharedModuleImpl()

    }
}