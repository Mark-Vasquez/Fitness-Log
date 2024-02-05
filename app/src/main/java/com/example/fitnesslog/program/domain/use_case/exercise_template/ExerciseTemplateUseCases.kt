package com.example.fitnesslog.program.domain.use_case.exercise_template

data class ExerciseTemplateUseCases(
    val createExerciseTemplate: CreateExerciseTemplate,
    val getExerciseTemplates: GetExerciseTemplates,
    val editExerciseTemplate: EditExerciseTemplate,
    val tryDeleteExerciseTemplate: TryDeleteExerciseTemplate,
    val addExercisesToWorkoutTemplate: AddExercisesToWorkoutTemplate,
    val getExercisesForWorkoutTemplate: GetExercisesForWorkoutTemplate,
    val reorderExercisesForWorkoutTemplate: ReorderExercisesForWorkoutTemplate,
    val deleteExerciseFromWorkoutTemplate: DeleteExerciseFromWorkoutTemplate
)
