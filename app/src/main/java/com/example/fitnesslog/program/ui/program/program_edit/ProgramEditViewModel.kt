package com.example.fitnesslog.program.ui.program.program_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.use_case.program.ProgramUseCases
import com.example.fitnesslog.program.domain.use_case.workout_template.WorkoutTemplateUseCases
import com.example.fitnesslog.program.ui.program.WorkoutTemplatesState
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

    private val _programEditState = MutableStateFlow(ProgramEditState())
    val programState = _programEditState.asStateFlow()

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
        getProgram(programId)
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


            is ProgramEditEvent.Delete -> {
                deleteProgram()
            }
        }
    }

    private fun getProgram(programId: Int) {
        viewModelScope.launch {
            when (val resource = programUseCases.getProgram(programId)) {
                is Resource.Success -> {
                    val program = resource.data
                    _programEditState.value = programState.value.copy(
                        program = program,
                        name = program.name,
                        scheduledDays = program.scheduledDays,
                        restDurationSeconds = program.restDurationSeconds
                    )
                }

                is Resource.Error -> {
                    _programEditState.value = programState.value.copy(
                        error = resource.errorMessage ?: "Error Retrieving Program"
                    )
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

                        is Resource.Error -> _programEditState.update { it.copy(error = resource.errorMessage) }
                    }
                }
        }
    }

    private fun checkIfDeletable() {
        viewModelScope.launch {
            when (val resource = programUseCases.checkIfDeletable()) {
                is Resource.Success -> {
                    _programEditState.value =
                        programState.value.copy(isDeletable = resource.data)
                }

                is Resource.Error -> {
                    _programEditState.value = programState.value.copy(
                        error = resource.errorMessage ?: "Error checking if deletable"
                    )
                }
            }
        }
    }


    private fun updateName(name: String) {
        _programEditState.value = programState.value.copy(
            name = name
        )
    }

    private fun updateScheduledDays(scheduledDays: Set<Day>) {
        _programEditState.value = programState.value.copy(
            scheduledDays = scheduledDays
        )
    }

    private fun updateRestDurationSeconds(restDurationSeconds: Int) {
        _programEditState.value = programState.value.copy(
            restDurationSeconds = restDurationSeconds
        )
    }


    private fun deleteProgram() {
        val programId = programState.value.program?.id ?: return
        viewModelScope.launch {
            programUseCases.deleteProgram(programId)
        }
    }


}