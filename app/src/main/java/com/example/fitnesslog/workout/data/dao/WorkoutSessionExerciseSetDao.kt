package com.example.fitnesslog.workout.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fitnesslog.workout.data.entity.WorkoutSessionExerciseSet
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionExerciseSetDao {
    @Insert
    suspend fun insertWorkoutSessionExerciseSet(workoutSessionExerciseSet: WorkoutSessionExerciseSet): Long

    @Insert
    suspend fun insertWorkoutSessionExerciseSets(workoutSessionExerciseSets: List<WorkoutSessionExerciseSet>): List<Long>

    @Query(
        """
        SELECT * FROM workout_session_exercise_set
        WHERE workout_session_exercise_id = :workoutSessionExerciseId
        ORDER BY position
        """
    )
    fun getSetsForWorkoutSessionExerciseOrderedByPosition(workoutSessionExerciseId: Int): Flow<List<WorkoutSessionExerciseSet>>

    @Update
    suspend fun updateWorkoutSessionExerciseSet(workoutSessionExerciseSet: WorkoutSessionExerciseSet): Int

    @Query("DELETE FROM workout_template_exercise_set WHERE id = :workoutTemplateExerciseSetId")
    suspend fun deleteWorkoutSessionExerciseSet(workoutTemplateExerciseSetId: Int): Int
}