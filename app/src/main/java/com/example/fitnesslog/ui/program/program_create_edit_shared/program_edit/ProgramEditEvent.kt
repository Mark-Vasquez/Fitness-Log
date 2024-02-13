package com.example.fitnesslog.ui.program.program_create_edit_shared.program_edit

import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.data.entity.WorkoutTemplate

sealed class ProgramEditEvent {

    data class UpdateName(val name: String) : ProgramEditEvent()
    data class UpdateScheduledDays(val scheduledDays: Set<Day>) : ProgramEditEvent()
    data class UpdateRestDurationSeconds(val restDurationSeconds: Int) : ProgramEditEvent()
    data class UpdateWorkoutTemplatesOrder(val workoutTemplates: List<WorkoutTemplate>) :
        ProgramEditEvent()

    data object CreateWorkoutTemplate : ProgramEditEvent()
    data object Delete : ProgramEditEvent()
}