package com.example.fitnesslog.exercise.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.exercise.data.model.ExerciseTemplate
import com.example.fitnesslog.exercise.data.model.WorkoutExerciseWithTemplateName
import com.example.fitnesslog.exercise.data.model.WorkoutTemplateExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    // **Exercise Template**

    @Insert
    suspend fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate): Long

    @Query("SELECT * FROM exercise_template ORDER BY name")
    fun getAllExercisesOrderedByName(): Flow<List<ExerciseTemplate>>

    @Update
    suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): Int

    @Transaction
    suspend fun deleteExerciseTemplateAndRearrange()

    @Delete
    suspend fun deleteExerciseTemplate(exerciseTemplate: ExerciseTemplate): Int


    // **Exercises for Specific Workout Template**

    @Transaction
    suspend fun addExercisesToWorkoutTemplate(
        exerciseTemplateIds: List<Int>,
        workoutTemplateId: Int
    ) {
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
        insertExercisesIntoWorkoutTemplate(workoutTemplateExercises)
    }

    @Insert
    suspend fun insertExercisesIntoWorkoutTemplate(exerciseTemplates: List<WorkoutTemplateExercise>): Long

    @Query("SELECT COUNT(*) FROM workout_template_exercise WHERE workout_template_id = :workoutTemplateId")
    suspend fun getPositionForInsert(workoutTemplateId: Int): Int

    @Query(
        """
        SELECT workout_template_exercise.*, exercise_template.name
        FROM workout_template_exercise
        JOIN exercise_template
        ON exercise_template.id = workout_template_exercise.exercise_template_id
        WHERE  workout_template_exercise.workout_template_id = :workoutTemplateId
        ORDER BY workout_template_exercise.position
    """
    )
    fun getExercisesForWorkoutTemplateOrderedByPosition(workoutTemplateId: Int): Flow<List<WorkoutExerciseWithTemplateName>>

    @Transaction
    suspend fun updateExercisePositionsForWorkoutTemplate(
        exerciseTemplates: List<ExerciseTemplate>,
        workoutTemplateId: Int
    ) {
        // Assign invalid positions to the UI ordered list to avoid unique pair constraints
        var invalidPosition: Int = -1
        exerciseTemplates.forEach { exerciseTemplate ->
            exerciseTemplate.id?.let {
                updateExercisePositionInWorkoutTemplate(
                    workoutTemplateId,
                    it, invalidPosition
                )
            }
            invalidPosition--
        }

        // Assign UI ordered positions by index
        exerciseTemplates.forEachIndexed { index, exerciseTemplate ->
            exerciseTemplate.id?.let {
                updateExercisePositionInWorkoutTemplate(
                    workoutTemplateId,
                    it, index
                )
            }
        }
    }

    @Query(
        """
        UPDATE workout_template_exercise
        SET position = :newPosition
        WHERE workout_template_id = :workoutTemplateId 
        AND exercise_template_id = :exerciseTemplateId
        """
    )
    suspend fun updateExercisePositionInWorkoutTemplate(
        workoutTemplateId: Int,
        exerciseTemplateId: Int,
        newPosition: Int
    )

    @Transaction
    suspend fun deleteExerciseFromWorkoutTemplateAndRearrange(exerciseTemplate: ExerciseTemplate)

    @Delete
    suspend fun deleteExerciseFromWorkoutTemplate(workoutTemplateExercise: WorkoutTemplateExercise)
}