package com.example.fitnesslog.program.data.repository

import android.util.Log
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.core.utils.safeCall
import com.example.fitnesslog.core.utils.toErrorMessage
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount
import com.example.fitnesslog.program.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Repository's job is to provide data to app's ui components any way it wants.
 * Abstracts origin of data and can come from a Room database, API, cache, shared preferences, etc.
 * Can be used to combine local and remote data like from a cache
 */
class ProgramRepositoryImpl(
    private val dao: ProgramDao,
) : ProgramRepository {


    override suspend fun insertProgram(program: Program): Resource<Long> {
        return safeCall { dao.insertProgram((program)) }
    }

    override fun getAllProgramsOrderedBySelected(): Flow<Resource<List<ProgramWithWorkoutCount>>> {
        return dao.getAllProgramsOrderedBySelected()
            .map { Resource.Success(it) as Resource<List<ProgramWithWorkoutCount>> }
            .catch { e -> emit(Resource.Error(e.toErrorMessage())) }
    }

    override fun getSelectedProgram(): Flow<Resource<Program>> {
        return dao.getSelectedProgram()
            .map { Resource.Success(it) as Resource<Program> }
            .catch { e -> emit(Resource.Error(e.toErrorMessage())) }
    }

    override suspend fun updateProgram(program: Program): Resource<Int> {
        Log.d("ProgramRepoImpl", program.name)
        return safeCall { dao.updateProgram(program) }
    }

    override suspend fun deleteProgram(programId: Int): Resource<Unit> {
        return safeCall { dao.deleteProgram(programId) }
    }

    override suspend fun setProgramAsSelected(programId: Int): Resource<Unit> {
        return safeCall { dao.setProgramAsSelected(programId) }
    }
}