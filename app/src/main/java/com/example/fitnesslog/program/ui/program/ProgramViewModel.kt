package com.example.fitnesslog.program.ui.program

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.use_case.ProgramUseCases
import com.example.fitnesslog.program.ui.ProgramMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProgramViewModel(
    private val programUseCases: ProgramUseCases
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ProgramState())
    val stateFlow = _stateFlow.asStateFlow()

    companion object {
        const val TAG = "ProgramViewModel"
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProgramViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ProgramViewModel(programModule.programUseCases) as T
                }
                throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
            }

        }
    }


    fun onEvent(event: ProgramEvent) {
        when (event) {
            is ProgramEvent.CreateMode -> {
                initializeProgram()
                _stateFlow.value = stateFlow.value.copy(programMode = event.mode)
            }

            is ProgramEvent.EditMode -> {
                getProgram(event.programId)
                _stateFlow.value = stateFlow.value.copy(programMode = event.mode)
            }

            is ProgramEvent.Save -> {
                saveCreate()
            }

            is ProgramEvent.Cancel -> {
                when (stateFlow.value.programMode) {
                    ProgramMode.CREATE -> cancelCreate()
                    ProgramMode.EDIT -> return
                }

            }

            is ProgramEvent.UpdateName -> {
                updateName(event.name)
            }

            is ProgramEvent.UpdateScheduledDays -> {
                updateScheduledDays(event.scheduledDays)
            }

            is ProgramEvent.UpdateRestDurationSeconds -> {
                updateRestDurationSeconds(event.restDurationSeconds)
            }
        }
    }

    private fun initializeProgram() {
        if (stateFlow.value.program != null) {
            // Program has already been initialized, in first fragment onCreate
            return
        }
        viewModelScope.launch {
            when (val resource = programUseCases.initializeProgram()) {
                is Resource.Success -> {
                    val programId = resource.data.toInt()
                    getProgram(programId)
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


    private fun getProgram(programId: Int) {
        viewModelScope.launch {
            when (val resource = programUseCases.getProgram(programId).first()) {
                is Resource.Success -> {
                    val program = resource.data
                    _stateFlow.value = stateFlow.value.copy(
                        program = program,
                        name = program.name,
                        scheduledDays = program.scheduledDays,
                        restDurationSeconds = program.restDurationSeconds
                    )
                }

                is Resource.Error -> {
                    _stateFlow.value = stateFlow.value.copy(
                        error = resource.errorMessage ?: "Error Retrieving Program"
                    )
                }
            }

        }
    }

    // Calls an edit to the already initialized Program with the user inputs
    private fun saveCreate() {
        val oldProgram = stateFlow.value.program
        if (oldProgram == null) {
            _stateFlow.value =
                stateFlow.value.copy(error = "Error Saving null Program in `save`")
            return
        }
        val program = oldProgram.copy(
            name = stateFlow.value.name,
            scheduledDays = stateFlow.value.scheduledDays,
            restDurationSeconds = stateFlow.value.restDurationSeconds,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            when (val resource = programUseCases.editProgram(program)) {
                is Resource.Success -> {


                }

                is Resource.Error -> {
                    _stateFlow.value = stateFlow.value.copy(
                        error = resource.errorMessage ?: ""
                    )
                }
            }

        }
    }

    // Deletes the already initialized program instance along with associated workouts
    private fun cancelCreate() {
        val programId = stateFlow.value.program?.id
        if (programId == null) {
            _stateFlow.value =
                stateFlow.value.copy(error = "Program is null in `cancelCreate`")
            return
        }
        viewModelScope.launch {
            when (val resource = programUseCases.deleteProgram(programId)) {
                is Resource.Success -> {
                }

                is Resource.Error -> {
                    _stateFlow.value = stateFlow.value.copy(
                        error = resource.errorMessage
                            ?: "Error Discarding Program in `cancelCreate`"
                    )
                }
            }
        }
    }


}