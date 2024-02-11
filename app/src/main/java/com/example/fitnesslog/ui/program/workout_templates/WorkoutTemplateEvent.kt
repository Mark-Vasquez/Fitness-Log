package com.example.fitnesslog.ui.program.workout_templates

sealed class WorkoutTemplateEvent {
    data class UpdateName(val name: String) : WorkoutTemplateEvent()
    data class UpdateWorkoutTemplateExercisesOrder(val workoutTemplateExercises: List<com.example.fitnesslog.domain.model.WorkoutTemplateExerciseWithName>) :
        WorkoutTemplateEvent()

    data object Delete : WorkoutTemplateEvent()
}