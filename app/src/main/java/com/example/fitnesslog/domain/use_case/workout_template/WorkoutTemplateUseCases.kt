package com.example.fitnesslog.domain.use_case.workout_template

data class WorkoutTemplateUseCases(
    val createWorkoutTemplate: CreateWorkoutTemplate,
    val getWorkoutTemplates: GetWorkoutTemplates,
    val getWorkoutTemplate: GetWorkoutTemplate,
    val editWorkoutTemplate: EditWorkoutTemplate,
    val reorderWorkoutTemplates: ReorderWorkoutTemplates,
    val deleteWorkoutTemplate: DeleteWorkoutTemplate,
    val addExercisesToWorkoutTemplate: AddExercisesToWorkoutTemplate,
    val getExercisesForWorkoutTemplate: GetExercisesForWorkoutTemplate,
    val getExerciseByIdForWorkoutTemplate: GetExerciseByIdForWorkoutTemplate,
    val reorderExercisesForWorkoutTemplate: ReorderExercisesForWorkoutTemplate,
    val deleteExerciseFromWorkoutTemplate: DeleteExerciseFromWorkoutTemplate,
    val getSetsForWorkoutTemplateExerciseId: GetSetsForWorkoutTemplateExerciseId,
    val editTemplateExerciseSet: EditTemplateExerciseSet,
    val deleteSetFromWorkoutTemplateExercise: DeleteSetFromWorkoutTemplateExercise,
    val addNewSet: AddNewSet
)
