package com.example.fitnesslog.ui.program.programs.program_create_edit_shared.program_create

import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.data.entity.WorkoutTemplate

sealed class ProgramCreateEvent {
    data object Save : ProgramCreateEvent()
    data object Cancel : ProgramCreateEvent()
    data class UpdateName(val name: String) : ProgramCreateEvent()
    data class UpdateScheduledDays(val scheduledDays: Set<Day>) : ProgramCreateEvent()
    data class UpdateRestDurationSeconds(val restDurationSeconds: Int) : ProgramCreateEvent()
    data object CreateWorkoutTemplate : ProgramCreateEvent()
    data class UpdateWorkoutTemplatesOrder(val workoutTemplates: List<WorkoutTemplate>) :
        ProgramCreateEvent()
}