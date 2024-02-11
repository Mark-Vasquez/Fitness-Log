package com.example.fitnesslog.workout.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fitnesslog.workout.data.entity.WorkoutSession
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {
    @Insert
    suspend fun insertWorkoutSession(workoutSession: WorkoutSession): Long

    @Query("SELECT * FROM workout_session ORDER BY created_at DESC")
    fun getWorkoutSessionsOrderedByCreatedLast(): Flow<List<WorkoutSession>>

    @Update
    suspend fun updateWorkoutSession(workoutSession: WorkoutSession): Int

    @Query("DELETE FROM workout_session WHERE id = :workoutSessionId")
    suspend fun deleteWorkoutSession(workoutSessionId: Int)


}