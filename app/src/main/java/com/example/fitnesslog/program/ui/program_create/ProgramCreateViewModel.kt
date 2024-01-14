package com.example.fitnesslog.program.ui.program_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
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
                save(event.program)
            }

            is ProgramCreateEvent.Cancel -> {
                cancel(event.programId)
            }
        }
    }

    private fun initializeProgram() {
        viewModelScope.launch {
            when (val resource = programUseCases.initializeProgram()) {
                is Resource.Success -> {
                    _stateFlow.value = stateFlow.value.copy(initializedProgramId = resource.data)
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

    private fun save(program: Program) {
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

    private fun cancel(programId: Long) {
        viewModelScope.launch {
            when (val resource = programUseCases.deleteProgram(programId)) {
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