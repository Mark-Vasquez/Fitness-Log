package com.example.fitnesslog.ui.program.exercise_templates

sealed class ExerciseTemplateEvent {
    data class ToggleCheckbox(val exerciseTemplateId: Int) : ExerciseTemplateEvent()
    data object SubmitSelectedExercises : ExerciseTemplateEvent()
}