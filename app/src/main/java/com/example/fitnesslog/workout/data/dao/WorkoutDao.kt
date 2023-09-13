package com.example.fitnesslog.workout.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.workout.data.model.WorkoutTemplate

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

    @Delete
    fun deleteWorkoutTemplate(workoutTemplate: WorkoutTemplate)

    @Query("")
    fun getWorkoutTemplatesForProgramOrderedByPosition(programId: Int)

    @Query("UPDATE workout_template SET position = :newPosition WHERE id = :workoutTemplateId")
    fun updateWorkoutTemplatePosition(workoutTemplateId: Int, newPosition: Int)

    @Transaction
    fun updateWorkoutTemplatePositionsForProgram(
        workoutTemplates: List<WorkoutTemplate>,
        programId: Int
    ) {
        var invalidPosition: Int = 1
        workoutTemplates.forEachIndexed { index, workoutTemplate ->
            // Layer of protection ensures List<WorkoutTemplate> all belong to the specific program
            if (workoutTemplate.programId == programId) {
                workoutTemplate.id?.let { updateWorkoutTemplatePosition(it, invalidPosition) }
            }
            invalidPosition--
        }

        workoutTemplates.forEachIndexed { index, workoutTemplate ->
            // Layer of protection ensures List<WorkoutTemplate> all belong to the specific program
            if (workoutTemplate.programId == programId) {
                workoutTemplate.id?.let { updateWorkoutTemplatePosition(it, index) }
            }
        }
    }
}