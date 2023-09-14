package com.example.fitnesslog.workout.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.workout.data.model.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    // **Workout Template**

    // Use to set as last "position" on field for inserted workoutTemplate object
    @Query("SELECT COUNT(*) FROM workout_template WHERE program_id = :programId")
    fun getWorkoutTemplateCountForProgram(programId: Int): Int

    @Insert
    fun insertWorkoutTemplate(workoutTemplate: WorkoutTemplate)

    @Update
    fun updateWorkoutTemplate(workoutTemplate: WorkoutTemplate)

    // Method for deleteWorkoutTemplateAndRearrange @Transaction
    @Delete
    fun deleteWorkoutTemplate(workoutTemplate: WorkoutTemplate)

    @Transaction
    fun deleteWorkoutTemplateAndRearrange(workoutTemplate: WorkoutTemplate) {
        deleteWorkoutTemplate(workoutTemplate)

    }

    @Query("SELECT * FROM workout_template WHERE program_id = :programId ORDER BY position")
    fun getWorkoutTemplatesForProgramOrderedByPosition(programId: Int): Flow<List<WorkoutTemplate>>

    // Method for updateWorkoutTemplatePositionsForProgram @Transaction
    @Query("UPDATE workout_template SET position = :newPosition WHERE id = :workoutTemplateId")
    suspend fun updateWorkoutTemplatePosition(workoutTemplateId: Int, newPosition: Int)

    @Transaction
    suspend fun updateWorkoutTemplatePositionsForProgram(
        workoutTemplates: List<WorkoutTemplate>,
        programId: Int
    ) {
        // Assign invalid positions to the UI ordered list to avoid unique pair constraints
        var invalidPosition: Int = -1
        workoutTemplates.forEach { workoutTemplate ->
            // Ensure all templates belong to programId
            if (workoutTemplate.programId == programId) {
                workoutTemplate.id?.let { updateWorkoutTemplatePosition(it, invalidPosition) }
            }
            invalidPosition--
        }

        // Assign UI ordered positions by index
        workoutTemplates.forEachIndexed { index, workoutTemplate ->
            workoutTemplate.id?.let { updateWorkoutTemplatePosition(it, index) }
        }
    }
}