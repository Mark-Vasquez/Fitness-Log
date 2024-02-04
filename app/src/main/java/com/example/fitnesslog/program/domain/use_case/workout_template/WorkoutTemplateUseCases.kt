package com.example.fitnesslog.program.domain.use_case.workout_template

data class WorkoutTemplateUseCases(
    val createWorkoutTemplate: CreateWorkoutTemplate,
    val getWorkoutTemplates: GetWorkoutTemplates,
    val editWorkoutTemplate: EditWorkoutTemplate,
    val reorderWorkoutTemplates: ReorderWorkoutTemplates,
    val deleteWorkoutTemplate: DeleteWorkoutTemplate,
    val getWorkoutTemplate: GetWorkoutTemplate
)
