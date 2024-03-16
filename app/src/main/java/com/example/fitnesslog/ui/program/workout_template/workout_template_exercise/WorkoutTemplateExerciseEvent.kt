package com.example.fitnesslog.ui.program.workout_template.workout_template_exercise

import com.example.fitnesslog.data.entity.WorkoutTemplateExerciseSet

sealed class WorkoutTemplateExerciseEvent {
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
}