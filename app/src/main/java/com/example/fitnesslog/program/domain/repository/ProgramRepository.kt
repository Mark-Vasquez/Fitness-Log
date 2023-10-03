package com.example.fitnesslog.program.domain.repository

import com.example.fitnesslog.program.data.entity.Program
import kotlinx.coroutines.flow.Flow

interface ProgramRepository {

    suspend fun insertProgram(program: Program): Long

    fun getAllProgramsOrderedBySelected(): Flow<List<Program>>

    suspend fun updateProgram(program: Program): Int

    suspend fun deleteProgram(program: Program): Int

    suspend fun setProgramAsSelected(programId: Int)
}