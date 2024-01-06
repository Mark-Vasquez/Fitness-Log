package com.example.fitnesslog.program.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.core.utils.safeCall
import com.example.fitnesslog.core.utils.toErrorMessage
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount
import com.example.fitnesslog.program.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Repository's job is to provide data to app's ui components any way it wants.
 * Abstracts origin of data and can come from a Room database, API, cache, shared preferences, etc.
 * Can be used to combine local and remote data like from a cache
 */
class ProgramRepositoryImpl(
    private val dao: ProgramDao,
    private val dataStore: DataStore<Preferences>
) : ProgramRepository {

    private val IS_SEEDED = booleanPreferencesKey("is_seeded")
    private suspend fun isSeeded(): Boolean {
        val preferences = dataStore.data.first()
        return preferences[IS_SEEDED] ?: false
    }

    override suspend fun seedDatabaseIfFirstRun(): Resource<Unit> {
        return safeCall {
            if (!isSeeded()) {
                val defaultProgram = Program(
                    name = "Sample Push Pull Legs Program",
                    restDurationSeconds = 90,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                insertProgram(defaultProgram)
                dataStore.edit { settings ->
                    settings[IS_SEEDED] = true
                }
            }
        }
    }


    override suspend fun insertProgram(program: Program): Resource<Long> {
        return safeCall { dao.insertProgram((program)) }
    }

    override fun getAllProgramsOrderedBySelected(): Flow<Resource<List<ProgramWithWorkoutCount>>> {
        return dao.getAllProgramsOrderedBySelected()
            .map { Resource.Success(it) as Resource<List<ProgramWithWorkoutCount>> }
            .catch { e -> emit(Resource.Error(e.toErrorMessage())) }
    }

    override suspend fun updateProgram(program: Program): Resource<Int> {
        return safeCall { dao.updateProgram(program) }
    }

    override suspend fun deleteProgram(program: Program): Resource<Int> {
        return safeCall { dao.deleteProgram(program) }
    }

    override suspend fun setProgramAsSelected(programId: Int): Resource<Unit> {
        return safeCall { dao.setProgramAsSelected(programId) }
    }
}