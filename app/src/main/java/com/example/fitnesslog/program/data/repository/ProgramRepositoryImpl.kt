package com.example.fitnesslog.program.data.repository

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

class ProgramRepositoryImpl(private val dao: ProgramDao) : ProgramRepository {
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