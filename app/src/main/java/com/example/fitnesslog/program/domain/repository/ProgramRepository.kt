package com.example.fitnesslog.program.domain.repository

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount
import kotlinx.coroutines.flow.Flow

/** Allows the UseCases to use the methods of the Repository as long as the RepositoryImpl instance is
 *following this interface contract. The implementation is loosely coupled and can change how it wants
 * to serve up and consume data without having to change anything on the useCase side
 */
interface ProgramRepository {


    suspend fun insertProgram(program: Program): Resource<Long>

    fun getAllProgramsOrderedBySelected(): Flow<Resource<List<ProgramWithWorkoutCount>>>

    fun getSelectedProgram(): Flow<Resource<Program>>

    suspend fun updateProgram(program: Program): Resource<Int>

    suspend fun deleteProgram(programId: Long): Resource<Unit>

    suspend fun setProgramAsSelected(programId: Int): Resource<Unit>
}