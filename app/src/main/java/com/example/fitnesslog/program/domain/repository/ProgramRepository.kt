package com.example.fitnesslog.program.domain.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import kotlinx.coroutines.flow.Flow

interface ProgramRepository {

    suspend fun insertProgram(program: Program): Resource<Long>

    fun getAllProgramsOrderedBySelected(): Flow<Resource<List<Program>>>

    suspend fun updateProgram(program: Program): Resource<Int>

    suspend fun deleteProgram(program: Program): Resource<Int>

    suspend fun setProgramAsSelected(programId: Int): Resource<Unit>
}