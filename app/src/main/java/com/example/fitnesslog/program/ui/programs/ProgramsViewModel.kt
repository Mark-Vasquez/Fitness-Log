package com.example.fitnesslog.program.ui.programs

import androidx.lifecycle.ViewModel
import com.example.fitnesslog.program.domain.use_case.ProgramUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProgramsViewModel(
    private val programUseCases: ProgramUseCases
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(ProgramsState())
    val stateFlow: StateFlow<ProgramsState> = _stateFlow

    fun onEvent(event: ProgramsEvent) {
        when (event) {
            is ProgramsEvent.Create -> {

            }

            is ProgramsEvent.Select -> {

            }

            is ProgramsEvent.Edit -> {

            }

            is ProgramsEvent.Delete -> {

            }
        }
    }
}