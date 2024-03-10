package com.example.fitnesslog.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fitnesslog.data.entity.ExerciseTemplate
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseTemplateDao {

    // **Exercise Template**
    @Insert
    suspend fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate): Long

    @Query("SELECT * FROM exercise_template ORDER BY name")
    fun getAllExercisesOrderedByName(): Flow<List<ExerciseTemplate>>

    @Query("SELECT * FROM exercise_template WHERE id = :exerciseTemplateId")
    fun getExerciseTemplateById(exerciseTemplateId: Int): Flow<ExerciseTemplate>

    @Query("SELECT * FROM exercise_template WHERE id in (:exerciseTemplateIds)")
    suspend fun getExercisesByIds(exerciseTemplateIds: List<Int>): List<ExerciseTemplate>

    @Update
    suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): Int

    // by id
    @Query("DELETE FROM exercise_template WHERE id = :exerciseTemplateId")
    suspend fun deleteExerciseTemplate(exerciseTemplateId: Int)

    @Query("DELETE FROM exercise_template WHERE id in (:exerciseTemplateIds)")
    suspend fun deleteExerciseTemplates(exerciseTemplateIds: List<Int>)
}
