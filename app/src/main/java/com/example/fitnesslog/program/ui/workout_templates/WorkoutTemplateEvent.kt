package com.example.fitnesslog.program.ui.workout_templates

import com.example.fitnesslog.program.domain.model.WorkoutTemplateExerciseWithName

sealed class WorkoutTemplateEvent {
    data class UpdateName(val name: String) : WorkoutTemplateEvent()
    data class UpdateWorkoutTemplateExercisesOrder(val workoutTemplateExercises: List<WorkoutTemplateExerciseWithName>) :
        WorkoutTemplateEvent()

    data object Delete : WorkoutTemplateEvent()
}