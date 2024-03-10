package com.example.fitnesslog.ui.program.exercise_templates

sealed class ExerciseTemplateEvent {
    data class ToggleTemplateSelect(val exerciseTemplateId: Int, val isDefault: Boolean) :
        ExerciseTemplateEvent()

    data class AddToTemplateSelect(val exerciseTemplateId: Int, val isDefault: Boolean) :
        ExerciseTemplateEvent()

    data class RemoveFromTemplateSelect(val exerciseTemplateId: Int) : ExerciseTemplateEvent()
    data object SubmitSelectedExercises : ExerciseTemplateEvent()
    data class DeleteSelectedExercises(val exerciseTemplateIds: List<Int>) : ExerciseTemplateEvent()
}