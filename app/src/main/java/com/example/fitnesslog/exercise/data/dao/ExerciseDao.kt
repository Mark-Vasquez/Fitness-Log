package com.example.fitnesslog.exercise.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.exercise.data.model.ExerciseTemplate
import com.example.fitnesslog.exercise.data.model.WorkoutTemplateExercise

@Dao
interface ExerciseDao {
    // **Exercise Template**

    @Insert
    fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate)

    @Update
    fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate)

    @Delete
    fun deleteExerciseTemplate(exerciseTemplate: ExerciseTemplate)

    @Query("")
    fun getAllExercisesOrderedByName()


    // **Exercises for Workout Template**

    // Use to set as last "position" on field for inserted workoutTemplateExercise object
    @Query("SELECT COUNT(*) FROM workout_template_exercise WHERE workout_template_id = :workoutTemplateId")
    fun getExerciseCountForWorkout(workoutTemplateId: Int): Int

    @Insert
    fun insertExerciseIntoWorkoutTemplate(workoutTemplateExercise: WorkoutTemplateExercise)

    @Delete
    fun deleteExerciseFromWorkoutTemplate(workoutTemplateExercise: WorkoutTemplateExercise)

    @Query("")
    fun getExercisesForWorkoutTemplateOrderedByPosition(workoutTemplateId: Int)

    @Query("UPDATE workout_template_exercise SET position = :newPosition WHERE workout_template_id = :workoutTemplateId AND exercise_template_id = :exerciseTemplateId")
    fun updateExercisePositionInWorkoutTemplate(
        workoutTemplateId: Int,
        exerciseTemplateId: Int,
        newPosition: Int
    )

    @Transaction
    fun updateExercisePositionsForWorkoutTemplate(
        exerciseTemplates: List<ExerciseTemplate>,
        workoutTemplateId: Int
    ) {
        /*
         Set invalid positions to each exercise belonging to the workout template to avoid
         unique pair violations
         */
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

        exerciseTemplates.forEachIndexed { index, exerciseTemplate ->
            exerciseTemplate.id?.let {
                updateExercisePositionInWorkoutTemplate(
                    workoutTemplateId,
                    it, index
                )
            }
        }
    }
}