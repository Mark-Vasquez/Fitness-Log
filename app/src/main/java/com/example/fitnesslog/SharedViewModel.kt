package com.example.fitnesslog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SharedViewModel(
    private val sharedUseCases: SharedUseCases
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(SharedState())
    val stateFlow: StateFlow<SharedState> = _stateFlow.asStateFlow()

    fun onEvent(event: SharedEvent) {
        clearError()
        when (event) {
            is SharedEvent.SelectProgram -> selectProgram(event.program)
        }
    }

    private fun selectProgram(program: ProgramWithWorkoutCount) {
        viewModelScope.launch {
            when (val resource = sharedUseCases.selectProgram(program.id)) {
                is Resource.Success -> {
                    _stateFlow.value = stateFlow.value.copy(
                        selectedProgram = program
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


    private fun clearError() {
        if (_stateFlow.value.error != null) {
            _stateFlow.value = stateFlow.value.copy(error = null)
        }
    }
}