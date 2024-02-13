package com.example.fitnesslog.ui.program.workout_template

import com.example.fitnesslog.data.entity.WorkoutTemplateExercise

sealed class WorkoutTemplateEvent {
    data class UpdateName(val name: String) : WorkoutTemplateEvent()
    data class UpdateWorkoutTemplateExercisesOrder(val workoutTemplateExercises: List<WorkoutTemplateExercise>) :
        WorkoutTemplateEvent()

    data object Delete : WorkoutTemplateEvent()
}