package com.example.fitnesslog.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.fitnesslog.core.data.database.FitnessLogDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

interface AppModule {
    val db: FitnessLogDatabase
    val dataStore: DataStore<Preferences>
}

private const val USER_SETTINGS_NAME = "user_settings_name"

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

    override val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = { appContext.preferencesDataStoreFile(USER_SETTINGS_NAME) }
    )
}