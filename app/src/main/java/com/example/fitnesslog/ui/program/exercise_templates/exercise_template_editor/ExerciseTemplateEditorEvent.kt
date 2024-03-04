package com.example.fitnesslog.ui.program.exercise_templates.exercise_template_editor

import com.example.fitnesslog.core.enums.ExerciseMuscle
import com.example.fitnesslog.core.enums.ExerciseResistance

sealed class ExerciseTemplateEditorEvent {
    data object InitializeExerciseTemplate : ExerciseTemplateEditorEvent()
    data object CancelCreate : ExerciseTemplateEditorEvent()
    data class UpdateName(val name: String) : ExerciseTemplateEditorEvent()
    data class UpdateExerciseMuscle(val exerciseMuscle: ExerciseMuscle) :
        ExerciseTemplateEditorEvent()

    data class UpdateExerciseResistance(val exerciseResistance: ExerciseResistance) :
        ExerciseTemplateEditorEvent()
}