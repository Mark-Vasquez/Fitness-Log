package com.example.fitnesslog.di

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


// The interface declares the contracts for the dependencies that will be provided by this module.
interface AppModule {
    val db: FitnessLogDatabase
    val dataStore: DataStore<Preferences>
}

private const val USER_SETTINGS = "user_settings"

/**
 * Provides single instances of dependencies for application wide injection.
 * When instantiated, it is injected with an appContext dependency to construct and supply
 * db and dataStore as singletons.
 */
class AppModuleImpl(
    private val appContext: Context
) : AppModule {
    // Lazily provides an instance of FitnessLogDatabase when requested, ensuring that
    // the database is only created once and the same instance is used throughout the app
    override val db: FitnessLogDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            FitnessLogDatabase::class.java,
            FitnessLogDatabase.DATABASE_NAME
        ).build()
    }

    /** Initializes and provides a DataStore for preferences with a boolean to ensure
     *  that initial seed data is only set once after the app's first launch
     *  This datastore is passed into the ProgramRepositoryImpl instance
     */
    override val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = { appContext.preferencesDataStoreFile(USER_SETTINGS) }
    )
}