package com.example.fitnesslog.ui.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.use_case.shared.SharedUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Scoped to the Activity, so all fragments can share state with cross-fragment communication
class SharedViewModel(
    private val sharedUseCases: SharedUseCases
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(SharedState())
    val stateFlow: StateFlow<SharedState> = _stateFlow.asStateFlow()

    companion object {
        const val TAG = "SharedViewModel"
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
                    return SharedViewModel(appModule.sharedUseCases) as T
                }
                throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
            }

        }
    }

    init {
        viewModelScope.launch {
//            if (!isSeeded()) {
//                seedInitialApplication()
//            }
            getSelectedProgram()
        }
    }

    fun onEvent(event: SharedEvent) {
        when (event) {
            is SharedEvent.ClearErrorState -> {
                clearErrorState()
            }
        }
    }

    private suspend fun seedInitialApplication() {
        val resource = sharedUseCases.seedInitialApplication()
        if (resource is Resource.Error) {
            _stateFlow.value = stateFlow.value.copy(
                error = resource.errorMessage ?: "Error Seeding Program on Launch"
            )
        }
    }

    private suspend fun getSelectedProgram() {
        sharedUseCases.getSelectedProgram().collectLatest { resource ->
            when (resource) {
                is Resource.Success -> {
                    val program = resource.data
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

    private fun clearErrorState() {
        if (_stateFlow.value.error != null) {
            _stateFlow.value = stateFlow.value.copy(error = null)
        }
    }
}