package com.example.fitnesslog.program.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {
    @Insert
    suspend fun insertProgram(program: Program): Long

    @Query(
        """
        SELECT 
            program.id as id,
            program.name as name,
            program.scheduled_days as scheduledDays,
            program.is_selected as isSelected,
            program.rest_duration_seconds as restDurationSeconds,
            COUNT(workout_template.program_id) as workoutCount
        FROM program 
        LEFT JOIN workout_template
        ON program.id = workout_template.program_id
        GROUP BY program.id
        ORDER BY is_selected DESC
        """
    )
    fun getAllProgramsOrderedBySelected(): Flow<List<ProgramWithWorkoutCount>>

    @Query("SELECT * FROM program WHERE id = :programId")
    fun getProgramById(programId: Int): Flow<Program>

    @Query("SELECT * FROM program WHERE is_selected = 1")
    fun getSelectedProgram(): Flow<Program>


    @Transaction
    suspend fun updateAndSelectProgram(program: Program) {
        updateProgram(program)
        program.id?.let { setProgramAsSelected(it) }
    }

    @Update
    suspend fun updateProgram(program: Program): Int

    @Query("DELETE FROM program WHERE id = :programId")
    suspend fun deleteProgram(programId: Int)

    @Query("SELECT COUNT(*) FROM program")
    suspend fun getProgramsCount(): Int

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