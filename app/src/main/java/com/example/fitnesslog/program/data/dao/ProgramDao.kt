package com.example.fitnesslog.program.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.program.data.model.Program
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {
    @Insert
    suspend fun insertProgram(program: Program): Long

    @Update
    suspend fun updateProgram(program: Program): Int

    @Delete
    suspend fun deleteProgram(program: Program): Int

    @Query("SELECT * FROM program ORDER BY is_selected DESC")
    fun getAllProgramsOrderedBySelected(): Flow<List<Program>>

    @Transaction
    suspend fun setProgramAsSelected(programId: Int) {
        deselectAllPrograms()
        selectProgram(programId)
    }

    @Query("UPDATE program SET is_selected = 0 WHERE is_selected = 1")
    suspend fun deselectAllPrograms()

    @Query("UPDATE program SET is_selected = 1 WHERE program.id = :programId")
    suspend fun selectProgram(programId: Int)

}