package com.example.fitnesslog.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.data.entity.WorkoutTemplate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface WorkoutTemplateDao {
    // **Workout Template**

    @Insert
    suspend fun insertWorkoutTemplate(workoutTemplate: WorkoutTemplate): Long

    @Query("SELECT COUNT(*) FROM workout_template WHERE program_id = :programId")
    suspend fun getPositionForInsert(programId: Int): Int

    @Query("SELECT * FROM workout_template WHERE id = :workoutTemplateId")
    fun getWorkoutTemplateById(workoutTemplateId: Int): Flow<WorkoutTemplate>

    @Query("SELECT * FROM workout_template WHERE program_id = :programId ORDER BY position")
    fun getWorkoutTemplatesForProgramOrderedByPosition(programId: Int): Flow<List<WorkoutTemplate>>

    @Update
    suspend fun updateWorkoutTemplate(workoutTemplate: WorkoutTemplate): Int

    @Transaction
    suspend fun updateAllWorkoutTemplatePositionsForProgram(workoutTemplates: List<WorkoutTemplate>) {
        // First assign invalid positions to the UI ordered list to avoid unique pair constraints
        var invalidPosition: Int = -1
        workoutTemplates.forEach { workoutTemplate ->
            workoutTemplate.id?.let { updateWorkoutTemplatePosition(it, invalidPosition) }
            invalidPosition--
        }

        // Then assign UI ordered positions by index
        workoutTemplates.forEachIndexed { index, workoutTemplate ->
            workoutTemplate.id?.let { updateWorkoutTemplatePosition(it, index) }
        }
    }

    @Query("UPDATE workout_template SET position = :newPosition WHERE id = :workoutTemplateId")
    suspend fun updateWorkoutTemplatePosition(workoutTemplateId: Int, newPosition: Int)


    @Transaction
    suspend fun deleteWorkoutTemplateInProgramAndRearrange(workoutTemplateId: Int, programId: Int) {
        deleteWorkoutTemplate(workoutTemplateId)

        val remainingWorkoutTemplatesInProgram =
            getWorkoutTemplatesForProgramOrderedByPosition(programId).first()

        if (remainingWorkoutTemplatesInProgram.isNotEmpty()) {
            updateAllWorkoutTemplatePositionsForProgram(remainingWorkoutTemplatesInProgram)
        }

    }

    @Query("DELETE FROM workout_template WHERE id = :workoutTemplateId")
    suspend fun deleteWorkoutTemplate(workoutTemplateId: Int)

}