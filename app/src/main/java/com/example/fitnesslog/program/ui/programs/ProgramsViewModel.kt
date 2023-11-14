package com.example.fitnesslog.program.ui.programs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.use_case.ProgramUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProgramsViewModel(
    private val programUseCases: ProgramUseCases
) : ViewModel() {


    init {
        seedProgram()
        getPrograms()
    }

    private val _stateFlow = MutableStateFlow(ProgramsState())
    val stateFlow: StateFlow<ProgramsState> = _stateFlow.asStateFlow()

    fun onEvent(event: ProgramsEvent) {
        when (event) {
            is ProgramsEvent.Create -> {
                // Launch the BottomSheetDialogFragment
            }

            is ProgramsEvent.Select -> {

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

    private fun getPrograms() {
        viewModelScope.launch {
            programUseCases.getPrograms().collect() { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _stateFlow.value = stateFlow.value.copy(
                            programs = resource.data
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

    private fun createProgram() {
        
    }
}

