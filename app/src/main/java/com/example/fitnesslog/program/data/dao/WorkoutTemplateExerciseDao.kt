package com.example.fitnesslog.program.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.program.data.entity.WorkoutTemplateExercise
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface WorkoutTemplateExerciseDao {

    @Insert
    suspend fun insertWorkoutTemplateExercises(workoutTemplateExercises: List<WorkoutTemplateExercise>): LongArray

    @Query("SELECT COUNT(*) FROM workout_template_exercise WHERE workout_template_id = :workoutTemplateId")
    suspend fun getPositionForInsert(workoutTemplateId: Int): Int

    @Query("SELECT * FROM workout_template_exercise WHERE workout_template_exercise.workout_template_id = :workoutTemplateId")
    fun getWorkoutTemplateExercisesOrderedByPosition(workoutTemplateId: Int): Flow<List<WorkoutTemplateExercise>>

    @Update
    suspend fun updateWorkoutTemplateExercise(workoutTemplateExercise: WorkoutTemplateExercise): Int

    @Transaction
    suspend fun updateAllExercisePositionsForWorkoutTemplate(workoutTemplateExercises: List<WorkoutTemplateExercise>) {
        // Assign invalid positions to the UI ordered list to avoid unique pair constraints
        var invalidPosition: Int = -1
        workoutTemplateExercises.forEach { workoutTemplateExercise ->
            workoutTemplateExercise.id?.let {
                updateWorkoutTemplateExercisePosition(
                    it,
                    invalidPosition
                )
            }
            invalidPosition--
        }

        // Assign UI ordered positions by index
        workoutTemplateExercises.forEachIndexed { index, workoutTemplateExercise ->
            workoutTemplateExercise.id?.let { updateWorkoutTemplateExercisePosition(it, index) }
        }
    }

    @Query(
        """
        UPDATE workout_template_exercise
        SET position = :newPosition
        WHERE id = :workoutTemplateExerciseId
        """
    )
    suspend fun updateWorkoutTemplateExercisePosition(
        workoutTemplateExerciseId: Int,
        newPosition: Int
    )

    @Transaction
    suspend fun deleteExerciseInWorkoutTemplateAndRearrange(
        workoutTemplateExerciseId: Int,
        workoutTemplateId: Int
    ) {
        deleteWorkoutTemplateExercise(workoutTemplateExerciseId)

        val remainingExercisesInWorkoutTemplate =
            getWorkoutTemplateExercisesOrderedByPosition(workoutTemplateId).first()

        if (remainingExercisesInWorkoutTemplate.isNotEmpty()) {
            updateAllExercisePositionsForWorkoutTemplate(remainingExercisesInWorkoutTemplate)
        }
    }

    @Query("DELETE FROM workout_template_exercise WHERE id = :workoutTemplateExerciseId")
    suspend fun deleteWorkoutTemplateExercise(workoutTemplateExerciseId: Int)
}