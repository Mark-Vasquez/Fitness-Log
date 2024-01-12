package com.example.fitnesslog.exercise.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.exercise.data.entity.ExerciseTemplate
import com.example.fitnesslog.exercise.data.entity.WorkoutTemplateExercise
import com.example.fitnesslog.exercise.domain.model.WorkoutTemplateExerciseWithName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface ExerciseDao {

    // **Exercise Template**
    @Insert
    suspend fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate): Long

    @Query("SELECT * FROM exercise_template ORDER BY name")
    fun getAllExercisesOrderedByName(): Flow<List<ExerciseTemplate>>

    @Update
    suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): Int

    // TODO: wrap this function with a try/catch
    @Transaction
    suspend fun tryDeleteExerciseTemplate(exerciseTemplate: ExerciseTemplate): Boolean {
        val usageCount = exerciseTemplate.id?.let { countWorkoutsUsingExerciseTemplate(it) }
            ?: throw IllegalArgumentException("ExerciseTemplate ID is null")
        return if (usageCount > 0) {
            false
        } else {
            deleteExerciseTemplate(exerciseTemplate)
            true
        }
    }

    @Query(
        """
        SELECT COUNT(*)
        FROM workout_template_exercise
        WHERE exercise_template_id = :exerciseTemplateId
        """
    )
    suspend fun countWorkoutsUsingExerciseTemplate(exerciseTemplateId: Int): Int

    @Delete
    suspend fun deleteExerciseTemplate(exerciseTemplate: ExerciseTemplate): Int


    // **Exercises for Specific Workout Template**
    @Transaction
    suspend fun addExercisesToWorkoutTemplate(
        exerciseTemplateIds: List<Int>,
        workoutTemplateId: Int
    ): LongArray {
        val lastInsertPosition = getPositionForInsert(workoutTemplateId)
        val workoutTemplateExercises =
            exerciseTemplateIds.mapIndexed { index, exerciseTemplateId ->
                WorkoutTemplateExercise(
                    workoutTemplateId = workoutTemplateId,
                    exerciseTemplateId = exerciseTemplateId,
                    position = lastInsertPosition + index,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            }
        return insertExercisesIntoWorkoutTemplate(workoutTemplateExercises)
    }

    @Insert
    suspend fun insertExercisesIntoWorkoutTemplate(exerciseTemplates: List<WorkoutTemplateExercise>): LongArray

    @Query("SELECT COUNT(*) FROM workout_template_exercise WHERE workout_template_id = :workoutTemplateId")
    suspend fun getPositionForInsert(workoutTemplateId: Int): Int

    @Query(
        """
        SELECT 
            workout_template_exercise.id as id,
            exercise_template.name as name,
            workout_template_exercise.workout_template_id as workoutTemplateId,
            workout_template_exercise.exercise_template_id as exerciseTemplateId,
            workout_template_exercise.position as position,
            workout_template_exercise.created_at as createdAt,
            workout_template_exercise.updated_at as updatedAt
        FROM workout_template_exercise
        JOIN exercise_template
        ON exercise_template.id = workout_template_exercise.exercise_template_id
        WHERE  workout_template_exercise.workout_template_id = :workoutTemplateId
        ORDER BY workout_template_exercise.position
        """
    )
    fun getExercisesForWorkoutTemplateOrderedByPosition(workoutTemplateId: Int): Flow<List<WorkoutTemplateExerciseWithName>>

    @Transaction
    suspend fun updateExercisesForWorkoutTemplate(
        workoutTemplateId: Int,
        newExerciseList: List<WorkoutTemplateExerciseWithName>
    ) {
        val oldExerciseList =
            getExercisesForWorkoutTemplateOrderedByPosition(workoutTemplateId).first()
        val removedExercises = oldExerciseList - newExerciseList
    }

    suspend fun updateExercisePositionsForWorkoutTemplate(
        workoutExercises: List<WorkoutTemplateExerciseWithName>
    ) {
        // Assign invalid positions to the UI ordered list to avoid unique pair constraints
        var invalidPosition: Int = -1
        workoutExercises.forEach {
            updateExercisePositionInWorkoutTemplate(
                workoutTemplateExerciseId = it.id,
                invalidPosition
            )
            invalidPosition--
        }

        // Assign UI ordered positions by index
        workoutExercises.forEachIndexed { index, workoutExercise ->
            updateExercisePositionInWorkoutTemplate(
                workoutTemplateExerciseId = workoutExercise.id,
                index
            )
        }
    }

    @Query(
        """
        UPDATE workout_template_exercise
        SET position = :newPosition
        WHERE id = :workoutTemplateExerciseId
        """
    )
    suspend fun updateExercisePositionInWorkoutTemplate(
        workoutTemplateExerciseId: Int,
        newPosition: Int
    )

}