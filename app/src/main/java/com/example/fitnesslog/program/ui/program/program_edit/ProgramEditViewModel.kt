package com.example.fitnesslog.program.ui.program.program_edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.program.domain.use_case.program.ProgramUseCases
import com.example.fitnesslog.program.domain.use_case.workout_template.WorkoutTemplateUseCases
import com.example.fitnesslog.program.ui.program.WorkoutTemplatesState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgramEditViewModel(
    private val programId: Int,
    private val programUseCases: ProgramUseCases,
    private val workoutTemplateUseCases: WorkoutTemplateUseCases
) : ViewModel() {

    private val _programState = MutableStateFlow(ProgramEditState())
    val programState = _programState.asStateFlow()

    private val _workoutTemplatesState = MutableStateFlow(WorkoutTemplatesState())
    val workoutTemplatesState = _workoutTemplatesState.asStateFlow()
    
    companion object {
        const val TAG = "ProgramEditViewModel"

        class Factory(
            val programId: Int,
            private val programUseCases: ProgramUseCases,
            private val workoutTemplateUseCases: WorkoutTemplateUseCases
        ) : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProgramEditViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ProgramEditViewModel(
                        programId,
                        programUseCases,
                        workoutTemplateUseCases
                    ) as T
                }
                throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
            }

        }

    }

    init {
        collectLatestProgram(programId)
        checkIfDeletable()
        collectLatestWorkoutTemplatesByProgramId(programId)
    }


    fun onEvent(event: ProgramEditEvent) {
        when (event) {

            is ProgramEditEvent.UpdateName -> {
                updateName(event.name)
            }

            is ProgramEditEvent.UpdateScheduledDays -> {
                updateScheduledDays(event.scheduledDays)
            }

            is ProgramEditEvent.UpdateRestDurationSeconds -> {
                updateRestDurationSeconds(event.restDurationSeconds)
            }

            is ProgramEditEvent.UpdateWorkoutTemplateOrder -> {
                updateWorkoutTemplateOrder(event.workoutTemplates)
            }

            is ProgramEditEvent.Delete -> {
                deleteProgram()
            }
        }
    }

    private fun collectLatestProgram(programId: Int) {
        viewModelScope.launch {
            programUseCases.getProgram(programId).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val program = resource.data
                        Log.d(TAG, "Collecting Program: ${program}")
                        program?.let {
                            _programState.value = programState.value.copy(
                                program = program,
                            )
                        }
                    }

                    is Resource.Error -> {
                        _programState.value = programState.value.copy(
                            error = resource.errorMessage ?: "Error Retrieving Program"
                        )
                    }
                }
            }
        }
    }

    private fun collectLatestWorkoutTemplatesByProgramId(programId: Int) {
        viewModelScope.launch {
            workoutTemplateUseCases.getWorkoutTemplates(programId)
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> _workoutTemplatesState.update {
                            it.copy(
                                workoutTemplates = resource.data
                            )
                        }

                        is Resource.Error -> _programState.update { it.copy(error = resource.errorMessage) }
                    }
                }
        }
    }

    private fun checkIfDeletable() {
        viewModelScope.launch {
            when (val resource = programUseCases.checkIfDeletable()) {
                is Resource.Success -> {
                    _programState.update { it.copy(isDeletable = resource.data) }
                    _programState.value =
                        programState.value.copy(isDeletable = resource.data)
                }

                is Resource.Error -> {
                    _programState.value = programState.value.copy(
                        error = resource.errorMessage ?: "Error checking if deletable"
                    )
                }
            }
        }
    }


    private fun updateName(name: String) {
        viewModelScope.launch {
            val currentProgram = programState.value.program
            currentProgram?.let {
                // Only update if text input is not the current Program name state
                if (name != currentProgram.name) {
                    programUseCases.editProgram(
                        it.copy(
                            name = name, updatedAt = System.currentTimeMillis()
                        )
                    )
                }
            }

        }
    }

    private fun updateScheduledDays(scheduledDays: Set<Day>) {
        viewModelScope.launch {
            val currentProgram = programState.value.program
            currentProgram?.let {
                if (scheduledDays != currentProgram.scheduledDays) {
                    programUseCases.editProgram(
                        it.copy(
                            scheduledDays = scheduledDays,
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }

    private fun updateRestDurationSeconds(restDurationSeconds: Int) {
        viewModelScope.launch {
            val currentProgram = programState.value.program
            currentProgram?.let {
                if (restDurationSeconds != currentProgram.restDurationSeconds) {
                    programUseCases.editProgram(
                        it.copy(
                            restDurationSeconds = restDurationSeconds,
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }

    private fun updateWorkoutTemplateOrder(workoutTemplates: List<WorkoutTemplate>) {
        viewModelScope.launch {
            val programId = programState.value.program?.id
            programId?.let { workoutTemplateUseCases.reorderWorkoutTemplates(workoutTemplates, it) }
        }
    }


    private fun deleteProgram() {
        val programId = programState.value.program?.id ?: return
        viewModelScope.launch {
            programUseCases.deleteProgram(programId)
        }
    }


}