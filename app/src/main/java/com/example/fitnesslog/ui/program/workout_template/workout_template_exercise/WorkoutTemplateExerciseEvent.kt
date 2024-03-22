package com.example.fitnesslog.ui.program.workout_template.workout_template_exercise

import com.example.fitnesslog.data.entity.WorkoutTemplateExerciseSet

sealed class WorkoutTemplateExerciseEvent {
    data object AddNewSet : WorkoutTemplateExerciseEvent()
    data class UpdateSetGoalRep(
        val workoutTemplateExerciseSet: WorkoutTemplateExerciseSet,
        val newGoalRep: Int
    ) :
        WorkoutTemplateExerciseEvent()

    data class UpdateSetWeight(
        val workoutTemplateExerciseSet: WorkoutTemplateExerciseSet,
        val newWeight: Int
    ) :
        WorkoutTemplateExerciseEvent()

    data class DeleteSet(
        val workoutTemplateExerciseSetId: Int,
        val workoutTemplateExerciseId: Int
    ) : WorkoutTemplateExerciseEvent()

    data object DeleteWorkoutTemplateExercise : WorkoutTemplateExerciseEvent()
}