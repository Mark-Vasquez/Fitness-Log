package com.example.fitnesslog.domain.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.Program
import kotlinx.coroutines.flow.Flow

/** Allows the UseCases to use the methods of the Repository as long as the RepositoryImpl instance is
 *following this interface contract. The implementation is loosely coupled and can change how it wants
 * to serve up and consume data without having to change anything on the useCase side
 */
interface ProgramRepository {


    suspend fun insertProgram(program: Program): Resource<Long>

    fun getAllProgramsOrderedBySelected(): Flow<Resource<List<com.example.fitnesslog.domain.model.ProgramWithWorkoutCount>>>

    suspend fun getProgramById(programId: Int): Flow<Resource<Program>>

    fun getSelectedProgram(): Flow<Resource<Program>>

    suspend fun updateAndSelectProgram(program: Program): Resource<Unit>

    suspend fun deleteProgram(programId: Int): Resource<Unit>

    suspend fun getProgramsCount(): Resource<Int>

    suspend fun setProgramAsSelected(programId: Int): Resource<Unit>
}