package com.example.fitnesslog.program.ui.workout_templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.use_case.exercise_template.ExerciseTemplateUseCases
import com.example.fitnesslog.program.domain.use_case.workout_template.WorkoutTemplateUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutTemplateViewModel(
    workoutTemplateId: Int,
    private val workoutTemplateUseCases: WorkoutTemplateUseCases,
    private val exerciseTemplateUseCases: ExerciseTemplateUseCases
) : ViewModel() {

    private val _workoutTemplateState = MutableStateFlow(WorkoutTemplateState())
    val workoutTemplateState = _workoutTemplateState.asStateFlow()

    private val _workoutTemplateExercisesState = MutableStateFlow(WorkoutTemplateExercisesState())
    val workoutTemplateExercisesState = _workoutTemplateExercisesState.asStateFlow()

    companion object {
        const val TAG = "WorkoutTemplateViewModel"
    }

    class Factory(
        private val programId: Int,
        private val workoutTemplateUseCases: WorkoutTemplateUseCases,
        private val exerciseTemplateUseCases: ExerciseTemplateUseCases
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorkoutTemplateViewModel::class.java)) {
                return WorkoutTemplateViewModel(
                    programId,
                    workoutTemplateUseCases,
                    exerciseTemplateUseCases
                ) as T
            }
            throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
        }
    }

    init {
        collectLatestWorkoutTemplate(workoutTemplateId)
        collectLatestWorkoutTemplateExercise(workoutTemplateId)
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

    private fun collectLatestWorkoutTemplateExercise(workoutTemplateId: Int) {
        viewModelScope.launch {
            exerciseTemplateUseCases.getExercisesForWorkoutTemplate(workoutTemplateId)
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> _workoutTemplateExercisesState.update {
                            it.copy(
                                workoutTemplateExercises = resource.data
                            )
                        }

                        is Resource.Error -> _workoutTemplateState.update { it.copy(error = resource.errorMessage) }
                    }
                }
        }
    }
}