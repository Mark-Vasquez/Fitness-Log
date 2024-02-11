package com.example.fitnesslog.domain.use_case.workout_template

data class WorkoutTemplateUseCases(
    val createWorkoutTemplate: com.example.fitnesslog.domain.use_case.workout_template.CreateWorkoutTemplate,
    val getWorkoutTemplates: com.example.fitnesslog.domain.use_case.workout_template.GetWorkoutTemplates,
    val editWorkoutTemplate: com.example.fitnesslog.domain.use_case.workout_template.EditWorkoutTemplate,
    val reorderWorkoutTemplates: com.example.fitnesslog.domain.use_case.workout_template.ReorderWorkoutTemplates,
    val deleteWorkoutTemplate: com.example.fitnesslog.domain.use_case.workout_template.DeleteWorkoutTemplate,
    val getWorkoutTemplate: com.example.fitnesslog.domain.use_case.workout_template.GetWorkoutTemplate
)
