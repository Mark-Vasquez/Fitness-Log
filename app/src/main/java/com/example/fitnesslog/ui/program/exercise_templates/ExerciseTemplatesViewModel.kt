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

    private val _selectedExercisesToIsDefaultState = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val selectedExercisesToIsDefaultState = _selectedExercisesToIsDefaultState.asStateFlow()

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
                toggleTemplateSelect(event.exerciseTemplateId, event.isDefault)
            }

            is ExerciseTemplateEvent.AddToTemplateSelect -> {
                addToTemplateSelect(event.exerciseTemplateId, event.isDefault)
            }

            is ExerciseTemplateEvent.RemoveFromTemplateSelect -> {
                removeFromTemplatesSelect(listOf(event.exerciseTemplateId))
            }

            ExerciseTemplateEvent.SubmitSelectedExercises -> {
                submitSelectedExercises()
            }

            is ExerciseTemplateEvent.DeleteSelectedExercises -> {
                deleteSelectedExercises(event.exerciseTemplateIds)
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

    private fun toggleTemplateSelect(exerciseTemplateId: Int, isDefault: Boolean) {
        val currentSelectedTemplateMap = selectedExercisesToIsDefaultState.value
        val newSelectedTemplateMap = currentSelectedTemplateMap.toMutableMap().apply {
            if (exerciseTemplateId in currentSelectedTemplateMap) {
                remove(exerciseTemplateId)
            } else {
                put(exerciseTemplateId, isDefault)
            }
        }
        _selectedExercisesToIsDefaultState.update {
            newSelectedTemplateMap.toMap()
        }
    }

    private fun addToTemplateSelect(exerciseTemplateId: Int, isDefault: Boolean) {
        val currentSelectedTemplateMap = selectedExercisesToIsDefaultState.value
        val newSelectedTemplateMap = currentSelectedTemplateMap.toMutableMap().apply {
            put(exerciseTemplateId, isDefault)
        }
        _selectedExercisesToIsDefaultState.update { newSelectedTemplateMap }
    }

    private fun removeFromTemplatesSelect(exerciseTemplateIds: List<Int>) {
        val currentSelectedTemplateMap = selectedExercisesToIsDefaultState.value
        val newSelectedTemplateMap = currentSelectedTemplateMap.toMutableMap()
        exerciseTemplateIds.forEach { exerciseTemplateId ->
            if (exerciseTemplateId in currentSelectedTemplateMap) {
                newSelectedTemplateMap.remove(exerciseTemplateId)

            }
        }
        _selectedExercisesToIsDefaultState.update { newSelectedTemplateMap }
    }

    private fun submitSelectedExercises() {
        viewModelScope.launch {
            val selectedTemplatesList = selectedExercisesToIsDefaultState.value.keys.toList()
            workoutTemplateUseCases.addExercisesToWorkoutTemplate(
                exerciseTemplateIds = selectedTemplatesList,
                workoutTemplateId = workoutTemplateId
            )
        }
    }

    private fun deleteSelectedExercises(exerciseTemplateIds: List<Int>) {
        viewModelScope.launch {
            when (val resource =
                exerciseTemplateUseCases.deleteExerciseTemplates(exerciseTemplateIds)) {
                is Resource.Success -> {
                    removeFromTemplatesSelect(exerciseTemplateIds)
                }

                is Resource.Error -> _exerciseTemplatesState.update { it.copy(error = resource.errorMessage) }
            }
        }
    }
}