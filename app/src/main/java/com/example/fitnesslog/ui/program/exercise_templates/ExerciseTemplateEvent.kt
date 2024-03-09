package com.example.fitnesslog.ui.program.exercise_templates

sealed class ExerciseTemplateEvent {
    data class ToggleTemplateSelect(val exerciseTemplateId: Int) : ExerciseTemplateEvent()
    data class AddToTemplateSelect(val exerciseTemplateId: Int) : ExerciseTemplateEvent()
    data class RemoveFromTemplateSelect(val exerciseTemplateId: Int) : ExerciseTemplateEvent()
    data object SubmitSelectedExercises : ExerciseTemplateEvent()
}