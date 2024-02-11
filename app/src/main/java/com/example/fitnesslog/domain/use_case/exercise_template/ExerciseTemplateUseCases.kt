package com.example.fitnesslog.domain.use_case.exercise_template

data class ExerciseTemplateUseCases(
    val createExerciseTemplate: com.example.fitnesslog.domain.use_case.exercise_template.CreateExerciseTemplate,
    val getExerciseTemplates: com.example.fitnesslog.domain.use_case.exercise_template.GetExerciseTemplates,
    val editExerciseTemplate: com.example.fitnesslog.domain.use_case.exercise_template.EditExerciseTemplate,
    val tryDeleteExerciseTemplate: com.example.fitnesslog.domain.use_case.exercise_template.TryDeleteExerciseTemplate,
    val addExercisesToWorkoutTemplate: com.example.fitnesslog.domain.use_case.exercise_template.AddExercisesToWorkoutTemplate,
    val getExercisesForWorkoutTemplate: com.example.fitnesslog.domain.use_case.exercise_template.GetExercisesForWorkoutTemplate,
    val reorderExercisesForWorkoutTemplate: com.example.fitnesslog.domain.use_case.exercise_template.ReorderExercisesForWorkoutTemplate,
    val deleteExerciseFromWorkoutTemplate: com.example.fitnesslog.domain.use_case.exercise_template.DeleteExerciseFromWorkoutTemplate
)
