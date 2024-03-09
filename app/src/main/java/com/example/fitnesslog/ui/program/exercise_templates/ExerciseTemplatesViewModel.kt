package com.example.fitnesslog.ui.program.exercise_templates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.use_case.exercise_template.ExerciseTemplateUseCases
import com.example.fitnesslog.domain.use_case.workout_template.WorkoutTemplateUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExerciseTemplatesViewModel(
    private val workoutTemplateId: Int,
    private val exerciseTemplateUseCases: ExerciseTemplateUseCases,
    private val workoutTemplateUseCases: WorkoutTemplateUseCases
) :
    ViewModel() {

    private val _exerciseTemplatesState = MutableStateFlow(ExerciseTemplatesState())
    val exerciseTemplatesState = _exerciseTemplatesState.asStateFlow()

    private val _selectedExerciseTemplatesState = MutableStateFlow<Set<Int>>(emptySet())
    val selectedExerciseTemplatesState = _selectedExerciseTemplatesState.asStateFlow()

    init {
        collectLatestExerciseTemplates()
    }

    class Factory(
        private val workoutTemplateId: Int,
        private val exerciseTemplateUseCases: ExerciseTemplateUseCases,
        private val workoutTemplateUseCases: WorkoutTemplateUseCases
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseTemplatesViewModel::class.java)) {
                return ExerciseTemplatesViewModel(
                    workoutTemplateId,
                    exerciseTemplateUseCases,
                    workoutTemplateUseCases
                ) as T
            }
            throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
        }
    }

    fun onEvent(event: ExerciseTemplateEvent) {
        when (event) {
            is ExerciseTemplateEvent.ToggleTemplateSelect -> {
                toggleTemplateSelect(event.exerciseTemplateId)
            }

            is ExerciseTemplateEvent.AddToTemplateSelect -> {
                addToTemplateSelect(event.exerciseTemplateId)
            }

            is ExerciseTemplateEvent.RemoveFromTemplateSelect -> {
                removeFromTemplateSelect(event.exerciseTemplateId)
            }

            ExerciseTemplateEvent.SubmitSelectedExercises -> {
                submitSelectedExercises()
            }

        }
    }

    private fun collectLatestExerciseTemplates() {
        viewModelScope.launch {
            exerciseTemplateUseCases.getExerciseTemplates().collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> _exerciseTemplatesState.update {
                        it.copy(
                            exerciseTemplates = resource.data
                        )
                    }

                    is Resource.Error -> _exerciseTemplatesState.update { it.copy(error = resource.errorMessage) }
                }
            }
        }
    }

    private fun toggleTemplateSelect(exerciseTemplateId: Int) {
        val newSelectedExerciseTemplates =
            if (exerciseTemplateId in selectedExerciseTemplatesState.value) {
                selectedExerciseTemplatesState.value - exerciseTemplateId
            } else {
                selectedExerciseTemplatesState.value + exerciseTemplateId
            }
        _selectedExerciseTemplatesState.update {
            newSelectedExerciseTemplates
        }
    }

    private fun addToTemplateSelect(exerciseTemplateId: Int) {
        val newSelectedExerciseTemplates = selectedExerciseTemplatesState.value + exerciseTemplateId
        _selectedExerciseTemplatesState.update { newSelectedExerciseTemplates }
    }

    private fun removeFromTemplateSelect(exerciseTemplateId: Int) {
        if (exerciseTemplateId in selectedExerciseTemplatesState.value) {
            val newSelectedExerciseTemplate =
                selectedExerciseTemplatesState.value - exerciseTemplateId
            _selectedExerciseTemplatesState.update { newSelectedExerciseTemplate }
        }
    }

    private fun submitSelectedExercises() {
        viewModelScope.launch {
            val selectedTemplatesList = selectedExerciseTemplatesState.value.toList()
            workoutTemplateUseCases.addExercisesToWorkoutTemplate(
                exerciseTemplateIds = selectedTemplatesList,
                workoutTemplateId = workoutTemplateId
            )
        }
    }
}