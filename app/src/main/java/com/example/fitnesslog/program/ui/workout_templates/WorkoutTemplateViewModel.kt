package com.example.fitnesslog.program.ui.workout_templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.use_case.workout_template.WorkoutTemplateUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutTemplateViewModel(
    workoutTemplateId: Int,
    private val workoutTemplateUseCases: WorkoutTemplateUseCases,
) : ViewModel() {

    private val _workoutTemplateState = MutableStateFlow(WorkoutTemplateState())
    val workoutTemplateState = _workoutTemplateState.asStateFlow()

    companion object {
        const val TAG = "WorkoutTemplateViewModel"
    }

    class Factory(
        private val programId: Int,
        private val workoutTemplateUseCases: WorkoutTemplateUseCases
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorkoutTemplateViewModel::class.java)) {
                return WorkoutTemplateViewModel(programId, workoutTemplateUseCases) as T
            }
            throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
        }
    }

    init {
        collectLatestWorkoutTemplate(workoutTemplateId)
    }

    private fun collectLatestWorkoutTemplate(workoutTemplateId: Int) {
        viewModelScope.launch {
            workoutTemplateUseCases.getWorkoutTemplate(workoutTemplateId)
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val workoutTemplate = resource.data
                            workoutTemplate?.let {
                                _workoutTemplateState.update {
                                    it.copy(
                                        workoutTemplate = workoutTemplate
                                    )
                                }
                            }
                        }

                        is Resource.Error -> {
                            _workoutTemplateState.update { it.copy(error = resource.errorMessage) }
                        }
                    }
                }
        }
    }
}