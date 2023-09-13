package com.example.fitnesslog.program.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.program.data.model.Program

@Dao
interface ProgramDao {
    @Insert
    fun insertProgram(program: Program)

    @Update
    fun updateProgram(program: Program)

    @Delete
    fun deleteProgram(program: Program)

    @Query("")
    fun getAllProgramsOrderedBySelected()

    /* Atomicity
     *
     */
    @Query("")
    fun deselectAllPrograms()

    @Query("")
    fun selectProgram(programId: Int)

    @Transaction
    fun setProgramAsSelected(programId: Int) {
        deselectAllPrograms()
        selectProgram(programId)
    }

}