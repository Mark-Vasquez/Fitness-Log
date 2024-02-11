package com.example.fitnesslog.workout.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fitnesslog.workout.data.entity.WorkoutSessionExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionExerciseDao {
    @Insert
    suspend fun insertWorkoutSessionExercise(workoutSessionExercise: WorkoutSessionExercise): Long

    @Insert
    suspend fun insertWorkoutSessionExercises(workoutSessionExercises: List<WorkoutSessionExercise>): List<Long>

    @Query(
        """
        SELECT * FROM workout_session_exercise 
        WHERE workout_session_id = :workoutSessionId
        ORDER BY position
        """
    )
    fun getExercisesForWorkoutSessionOrderedByPosition(workoutSessionId: Int): Flow<List<WorkoutSessionExercise>>

    @Update
    suspend fun updateWorkoutSessionExercise(workoutSessionExercise: WorkoutSessionExercise): Int

    @Query("DELETE FROM workout_template_exercise WHERE id = :workoutSessionExerciseId")
    suspend fun deleteWorkoutSessionExercise(workoutSessionExerciseId: Int): Int
}