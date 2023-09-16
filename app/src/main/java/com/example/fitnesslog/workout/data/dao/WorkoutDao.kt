package com.example.fitnesslog.workout.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.workout.data.model.WorkoutTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

@Dao
interface WorkoutDao {
    // **Workout Template**

    @Insert
    suspend fun insertWorkoutTemplate(workoutTemplate: WorkoutTemplate): Long

    @Query("SELECT COUNT(*) FROM workout_template WHERE program_id = :programId")
    suspend fun getPositionForInsert(programId: Int): Int

    @Query("SELECT * FROM workout_template WHERE program_id = :programId ORDER BY position")
    fun getWorkoutTemplatesForProgramOrderedByPosition(programId: Int): Flow<List<WorkoutTemplate>>

    @Update
    suspend fun updateWorkoutTemplate(workoutTemplate: WorkoutTemplate): Int

    @Transaction
    suspend fun updateWorkoutTemplatePositionsForProgram(
        workoutTemplates: List<WorkoutTemplate>,
        programId: Int
    ) {
        // First assign invalid positions to the UI ordered list to avoid unique pair constraints
        var invalidPosition: Int = -1
        workoutTemplates.forEach { workoutTemplate ->
            // Ensure all templates belong to programId
            if (workoutTemplate.programId == programId) {
                workoutTemplate.id?.let { updateWorkoutTemplatePosition(it, invalidPosition) }
            }
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
    suspend fun deleteWorkoutTemplateAndRearrange(workoutTemplate: WorkoutTemplate) {
        deleteWorkoutTemplate(workoutTemplate)

        val remainingWorkoutTemplates = withContext(Dispatchers.IO) {
            getWorkoutTemplatesForProgramOrderedByPosition(workoutTemplate.programId).firstOrNull()
                ?: listOf()
        }

        if (remainingWorkoutTemplates.isNotEmpty()) {
            updateWorkoutTemplatePositionsForProgram(
                remainingWorkoutTemplates,
                workoutTemplate.programId
            )
        }

    }

    @Delete
    suspend fun deleteWorkoutTemplate(workoutTemplate: WorkoutTemplate): Int

}