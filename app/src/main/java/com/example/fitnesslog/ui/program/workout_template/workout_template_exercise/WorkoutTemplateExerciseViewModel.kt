package com.example.fitnesslog.ui.program.workout_template.workout_template_exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.use_case.workout_template.WorkoutTemplateUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutTemplateExerciseViewModel(
    workoutTemplateExerciseId: Int,
    private val workoutTemplateUseCases: WorkoutTemplateUseCases
) : ViewModel() {

    private val _workoutTemplateExerciseState = MutableStateFlow(WorkoutTemplateExerciseState())
    val workoutTemplateExerciseState = _workoutTemplateExerciseState.asStateFlow()

    private val _workoutTemplateExerciseSetsState =
        MutableStateFlow(WorkoutTemplateExerciseSetsState())

    class Factory(
        private val workoutTemplateExerciseId: Int,
        private val workoutTemplateUseCases: WorkoutTemplateUseCases
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorkoutTemplateExerciseViewModel::class.java)) {
                return WorkoutTemplateExerciseViewModel(
                    workoutTemplateExerciseId,
                    workoutTemplateUseCases
                ) as T
            }
            throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
        }
    }

    init {
        collectLatestWorkoutTemplateExercise(workoutTemplateExerciseId)
        collectLatestWorkoutTemplateExerciseSets(workoutTemplateExerciseId)
    }

    private fun collectLatestWorkoutTemplateExercise(workoutTemplateExerciseId: Int) {
        viewModelScope.launch {
            workoutTemplateUseCases.getExerciseByIdForWorkoutTemplate(workoutTemplateExerciseId)
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val workoutTemplateExercise = resource.data
                            workoutTemplateExercise?.let {
                                _workoutTemplateExerciseState.update {
                                    it.copy(
                                        workoutTemplateExercise = workoutTemplateExercise
                                    )
                                }
                            }
                        }

                        is Resource.Error -> {
                            _workoutTemplateExerciseState.update { it.copy(error = resource.errorMessage) }
                        }
                    }
                }
        }
    }

    private fun collectLatestWorkoutTemplateExerciseSets(workoutTemplateExerciseId: Int) {

    }
}