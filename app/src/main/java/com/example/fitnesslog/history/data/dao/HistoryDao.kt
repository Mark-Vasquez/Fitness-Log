package com.example.fitnesslog.history.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fitnesslog.exercise.domain.model.WorkoutSessionExercise
import com.example.fitnesslog.exercise.domain.model.WorkoutSessionExerciseSet
import com.example.fitnesslog.workout.domain.model.WorkoutSession
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    // **Workout Session**
    @Insert
    suspend fun insertWorkoutSession(workoutSession: WorkoutSession): Long

    @Query("SELECT * FROM workout_session ORDER BY created_at")
    fun getWorkoutSessionsOrderedByCreatedAt(): Flow<List<WorkoutSession>>

    @Update
    suspend fun updateWorkoutSession(workoutSession: WorkoutSession): Int

    @Query("DELETE FROM workout_session WHERE id = :workoutSessionId")
    suspend fun deleteWorkoutSession(workoutSessionId: Int)


    // **Workout Session Exercise**
    @Insert
    suspend fun insertSessionExercise(workoutSessionExercise: WorkoutSessionExercise): Long

    @Insert
    suspend fun insertSessionExercises(workoutSessionExercises: List<WorkoutSessionExercise>): List<Long>

    @Query(
        """
        SELECT * FROM workout_session_exercise 
        WHERE workout_session_id = :workoutSessionId
        ORDER BY position
        """
    )
    fun getExercisesForWorkoutSessionOrderedByPosition(workoutSessionId: Int): Flow<List<WorkoutSessionExercise>>

    @Update
    suspend fun updateSessionExercise(workoutSessionExercise: WorkoutSessionExercise): Int

    @Delete
    suspend fun deleteSessionExercise(workoutSessionExercise: WorkoutSessionExercise): Int


    // **Workout Session Exercise Set**
    @Insert
    suspend fun insertSessionExerciseSet(workoutSessionExerciseSet: WorkoutSessionExerciseSet): Long

    @Insert
    suspend fun insertSessionExerciseSets(workoutSessionExerciseSets: List<WorkoutSessionExerciseSet>)

    @Query(
        """
        SELECT * FROM workout_session_exercise_set
        WHERE workout_session_exercise_id = :workoutSessionExerciseId
        ORDER BY position
        """
    )
    fun getSetsForExerciseOrderedByPosition(workoutSessionExerciseId: Int): Flow<List<WorkoutSessionExerciseSet>>
}