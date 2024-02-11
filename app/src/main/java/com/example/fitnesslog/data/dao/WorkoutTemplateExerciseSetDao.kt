package com.example.fitnesslog.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.data.entity.WorkoutTemplateExerciseSet
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface WorkoutTemplateExerciseSetDao {

    @Insert
    suspend fun insertSetTemplate(workoutTemplateExerciseSet: WorkoutTemplateExerciseSet): Long

    @Query("SELECT COUNT(*) FROM workout_template_exercise_set WHERE workout_template_exercise_id = :workoutTemplateExerciseId")
    suspend fun getPositionForInsert(workoutTemplateExerciseId: Int): Int

    @Query("SELECT * FROM workout_template_exercise_set WHERE workout_template_exercise_id = :workoutTemplateExerciseId ORDER BY position")
    fun getWorkoutTemplateExerciseSetsOrderedByPosition(workoutTemplateExerciseId: Int): Flow<List<WorkoutTemplateExerciseSet>>

    @Update
    suspend fun updateWorkoutTemplateExerciseSet(workoutTemplateExerciseSet: WorkoutTemplateExerciseSet)

    @Transaction
    suspend fun updateAllSetPositionsForWorkoutTemplateExercise(
        workoutTemplateExerciseSets: List<WorkoutTemplateExerciseSet>
    ) {
        var invalidPosition: Int = -1
        workoutTemplateExerciseSets.forEach { workoutTemplateExerciseSet ->
            workoutTemplateExerciseSet.id?.let {
                updateWorkoutTemplateExerciseSetPosition(
                    it,
                    invalidPosition
                )
            }
            invalidPosition--
        }

        workoutTemplateExerciseSets.forEachIndexed { index, workoutTemplateExerciseSet ->
            workoutTemplateExerciseSet.id?.let {
                updateWorkoutTemplateExerciseSetPosition(
                    it,
                    index
                )
            }
        }
    }

    @Query("UPDATE workout_template_exercise_set SET position = :newPosition WHERE id = :workoutTemplateExerciseSetId")
    suspend fun updateWorkoutTemplateExerciseSetPosition(
        workoutTemplateExerciseSetId: Int,
        newPosition: Int
    )

    // Rearranges after every delete, not save
    @Transaction
    suspend fun deleteSetInWorkoutTemplateExerciseAndRearrange(
        workoutTemplateExerciseSetId: Int,
        workoutTemplateExerciseId: Int
    ) {
        deleteWorkoutTemplateExerciseSet(workoutTemplateExerciseSetId)

        val remainingSetsInWorkoutTemplateExercise =
            getWorkoutTemplateExerciseSetsOrderedByPosition(workoutTemplateExerciseId).first()

        if (remainingSetsInWorkoutTemplateExercise.isNotEmpty()) {
            updateAllSetPositionsForWorkoutTemplateExercise(remainingSetsInWorkoutTemplateExercise)
        }

    }

    @Query("DELETE FROM workout_template_exercise_set WHERE id = :workoutTemplateExerciseSetId")
    suspend fun deleteWorkoutTemplateExerciseSet(workoutTemplateExerciseSetId: Int)
}