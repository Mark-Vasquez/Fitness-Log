package com.example.fitnesslog.program.ui.programs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount
import com.example.fitnesslog.program.domain.use_case.ProgramUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProgramsViewModel(
    private val programUseCases: ProgramUseCases,
) : ViewModel() {

    // _stateflow is mutable flow of ProgramsState, modified only internally
    private val _stateFlow = MutableStateFlow(ProgramsState())

    // stateflow is the immutable public API exposing the _stateflow as a read only flow
    // External observers can used this stateflow to subscribe to state changes
    // When _stateFlow's value changes, stateFlow will emit these changes to its collectors.
    val stateFlow: StateFlow<ProgramsState> = _stateFlow.asStateFlow()

    companion object {
        const val TAG = "ProgramsViewModel"
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProgramsViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ProgramsViewModel(programModule.programUseCases) as T
                }
                throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
            }

        }
    }


    init {
        seedProgram()
        collectLatestPrograms()
    }


    fun onEvent(event: ProgramsEvent) {
        when (event) {
            is ProgramsEvent.ShowCreateForm -> {
                viewModelScope.launch {
                    showCreateForm()
                }
            }

            is ProgramsEvent.ShowEditForm -> {

            }

            is ProgramsEvent.Create -> {
                createProgram(event.program)
            }

            is ProgramsEvent.Select -> {
                selectProgram(event.program)
            }

            is ProgramsEvent.Edit -> {

            }

            is ProgramsEvent.Delete -> {

            }

        }
    }


    private fun seedProgram() {
        viewModelScope.launch {
            val resource = programUseCases.seedProgram()
            if (resource is Resource.Error) {
                _stateFlow.value = stateFlow.value.copy(
                    error = resource.errorMessage
                )
            }
        }
    }

    private fun collectLatestPrograms() {
        // Use collectLatest because only want to render latest version of list and disregard intermediate changes
        viewModelScope.launch {
            // Collects Flow<Resource<List<ProgramWithWorkoutCount>>> and listens for changes emitted from db via flow
            programUseCases.getPrograms().collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val programs = resource.data
                        if (programs.isNotEmpty() && !programs.first().isSelected) {
                            // Ensure the first item in the list is always selected
                            onEvent(ProgramsEvent.Select(programs.first()))
                        }
                        _stateFlow.value = stateFlow.value.copy(
                            programs = programs
                        )
                    }

                    is Resource.Error -> {
                        _stateFlow.value = stateFlow.value.copy(
                            error = resource.errorMessage
                        )
                    }
                }
            }
        }
    }

    private fun showCreateForm() {
        viewModelScope.launch {
            when (val resource = programUseCases.createProgramWithDefaultValues()) {
                is Resource.Success -> {
                    _stateFlow.value = stateFlow.value.copy(newDefaultProgramId = resource.data)
                }

                is Resource.Error -> {
                    _stateFlow.value = stateFlow.value.copy(
                        error = resource.errorMessage
                            ?: "Error Creating Default Program in `showCreateForm`"
                    )
                }
            }

        }
    }

    private fun createProgram(program: Program) {
        viewModelScope.launch {

        }
    }


    private fun selectProgram(program: ProgramWithWorkoutCount) {
        viewModelScope.launch {
            val resource = programUseCases.selectProgram(program.id)
            if (resource is Resource.Error) {
                _stateFlow.value = stateFlow.value.copy(
                    error = resource.errorMessage ?: "Error Selecting Program"
                )
            }

        }
    }

}


