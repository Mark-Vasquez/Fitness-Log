package com.example.fitnesslog.domain.use_case.exercise_template

data class ExerciseTemplateUseCases(
    val createExerciseTemplate: CreateExerciseTemplate,
    val getExerciseTemplates: GetExerciseTemplates,
    val editExerciseTemplate: EditExerciseTemplate,
    val getExerciseTemplateById: GetExerciseTemplateById,
    val initializeExerciseTemplate: InitializeExerciseTemplate,
    val discardInitializedTemplate: DiscardInitializedTemplate,
    val deleteExerciseTemplate: DeleteExerciseTemplate,
    val deleteExerciseTemplates: DeleteExerciseTemplates
)
