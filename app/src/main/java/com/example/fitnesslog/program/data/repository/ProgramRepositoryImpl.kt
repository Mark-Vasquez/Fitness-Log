package com.example.fitnesslog.program.data.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.Flow

class ProgramRepositoryImpl(private val dao: ProgramDao) : ProgramRepository {
    override suspend fun insertProgram(program: Program): Resource<Long> {
        return Resource.Success(dao.insertProgram(program))
    }

    override fun getAllProgramsOrderedBySelected(): Flow<List<Program>> {
        return dao.getAllProgramsOrderedBySelected()
    }

    override suspend fun updateProgram(program: Program): Int {
        return dao.updateProgram(program)
    }

    override suspend fun deleteProgram(program: Program): Int {
        return dao.deleteProgram(program)
    }

    override suspend fun setProgramAsSelected(programId: Int) {
        dao.setProgramAsSelected(programId)
    }
}