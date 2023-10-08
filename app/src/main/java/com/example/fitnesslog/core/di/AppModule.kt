package com.example.fitnesslog.core.di

import android.content.Context
import androidx.room.Room
import com.example.fitnesslog.core.data.database.FitnessLogDatabase

interface AppModule {
    val db: FitnessLogDatabase
}

class AppModuleImpl(
    private val appContext: Context
) : AppModule {
    override val db: FitnessLogDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            FitnessLogDatabase::class.java,
            FitnessLogDatabase.DATABASE_NAME
        ).build()
    }


}