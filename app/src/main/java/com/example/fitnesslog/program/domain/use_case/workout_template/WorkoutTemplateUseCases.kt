package com.example.fitnesslog.program.domain.use_case.workout_template

data class WorkoutTemplateUseCases(
    val createWorkoutTemplate: CreateWorkoutTemplate,
    val getWorkoutTemplates: GetWorkoutTemplates,
    val editWorkoutTemplate: EditWorkoutTemplate,
    val reorderWorkoutTemplate: ReorderWorkoutTemplate,
    val deleteWorkoutTemplate: DeleteWorkoutTemplate
)
