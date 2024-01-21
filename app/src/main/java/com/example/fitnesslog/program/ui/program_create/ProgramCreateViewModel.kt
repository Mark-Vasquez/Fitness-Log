package com.example.fitnesslog.program.ui.program_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.use_case.ProgramUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProgramCreateViewModel(
    private val programUseCases: ProgramUseCases
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ProgramCreateState())
    val stateFlow = _stateFlow.asStateFlow()

    companion object {
        const val TAG = "ProgramCreateViewModel"
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProgramCreateViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ProgramCreateViewModel(programModule.programUseCases) as T
                }
                throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
            }

        }
    }

    init {
        initializeProgram()
    }

    fun onEvent(event: ProgramCreateEvent) {
        when (event) {
            is ProgramCreateEvent.Save -> {
                saveCreate()
            }

            is ProgramCreateEvent.Cancel -> {
                cancelCreate()
            }

            is ProgramCreateEvent.UpdateName -> {
                updateName(event.name)
            }

            is ProgramCreateEvent.UpdateScheduledDays -> {
                updateScheduledDays(event.scheduledDays)
            }

            is ProgramCreateEvent.UpdateRestDurationSeconds -> {
                updateRestDurationSeconds(event.restDurationSeconds)
            }
        }
    }

    private fun updateName(name: String) {
        _stateFlow.value = stateFlow.value.copy(
            name = name,
        )
    }

    private fun updateScheduledDays(scheduledDays: Set<Day>) {
        _stateFlow.value = stateFlow.value.copy(
            scheduledDays = scheduledDays
        )
    }

    private fun updateRestDurationSeconds(restDurationSeconds: Int) {
        _stateFlow.value = stateFlow.value.copy(
            restDurationSeconds = restDurationSeconds
        )
    }

    private fun initializeProgram() {
        viewModelScope.launch {
            when (val resource = programUseCases.initializeProgram()) {
                is Resource.Success -> {
                    _stateFlow.value =
                        stateFlow.value.copy(initializedProgramId = resource.data.toInt())
                }

                is Resource.Error -> {
                    _stateFlow.value = stateFlow.value.copy(
                        error = resource.errorMessage
                            ?: "Error Initializing Program in `$TAG`"
                    )
                }
            }
        }
    }

    // Calls an edit to the already initialized Program with the user inputs
    private fun saveCreate() {
        val initializedProgramId = stateFlow.value.initializedProgramId
        if (initializedProgramId == null) {
            _stateFlow.value =
                stateFlow.value.copy(error = "Error Retrieving `initializedProgramId`")
            return
        }
        val program = Program(
            id = stateFlow.value.initializedProgramId,
            name = stateFlow.value.name,
            scheduledDays = stateFlow.value.scheduledDays,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            when (val resource = programUseCases.editProgram(program)) {
                is Resource.Success -> {
                    _stateFlow.value = stateFlow.value.copy(
                        initializedProgramId = null
                    )

                }

                is Resource.Error -> {
                    _stateFlow.value = stateFlow.value.copy(
                        error = resource.errorMessage ?: "Error Creating Program in `save`"
                    )
                }
            }

        }
    }

    // Deletes the already initialized program instance along with associated workouts
    private fun cancelCreate() {
        val initializedProgramId = stateFlow.value.initializedProgramId
        if (initializedProgramId == null) {
            _stateFlow.value =
                stateFlow.value.copy(error = "Error Retrieving `initializedProgramId`")
            return
        }
        viewModelScope.launch {
            when (val resource = programUseCases.deleteProgram(initializedProgramId)) {
                is Resource.Success -> {
                    _stateFlow.value = stateFlow.value.copy(
                        initializedProgramId = null
                    )
                }

                is Resource.Error -> {
                    _stateFlow.value = stateFlow.value.copy(
                        error = resource.errorMessage ?: "Error Discarding Program in `cancel`"
                    )
                }
            }
        }
    }
}