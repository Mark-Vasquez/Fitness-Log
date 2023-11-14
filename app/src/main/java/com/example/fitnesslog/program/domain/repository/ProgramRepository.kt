package com.example.fitnesslog.program.domain.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount
import kotlinx.coroutines.flow.Flow

interface ProgramRepository {

    suspend fun seedDatabaseIfFirstRun(): Resource<Unit>

    suspend fun insertProgram(program: Program): Resource<Long>

    fun getAllProgramsOrderedBySelected(): Flow<Resource<List<ProgramWithWorkoutCount>>>

    suspend fun updateProgram(program: Program): Resource<Int>

    suspend fun deleteProgram(program: Program): Resource<Int>

    suspend fun setProgramAsSelected(programId: Int): Resource<Unit>
}