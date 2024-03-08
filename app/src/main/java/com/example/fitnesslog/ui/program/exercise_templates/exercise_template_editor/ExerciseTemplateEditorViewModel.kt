package com.example.fitnesslog.ui.program.exercise_templates.exercise_template_editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.core.enums.ExerciseMuscle
import com.example.fitnesslog.core.enums.ExerciseResistance
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.use_case.exercise_template.ExerciseTemplateUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExerciseTemplateEditorViewModel(private val exerciseTemplateUseCases: ExerciseTemplateUseCases) :
    ViewModel() {

    private val _exerciseTemplateState = MutableStateFlow(ExerciseTemplateState())
    val exerciseTemplateState = _exerciseTemplateState.asStateFlow()

    class Factory(private val exerciseTemplateUseCases: ExerciseTemplateUseCases) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseTemplateEditorViewModel::class.java)) {
                return ExerciseTemplateEditorViewModel(exerciseTemplateUseCases) as T
            }
            throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
        }
    }

    fun onEvent(event: ExerciseTemplateEditorEvent) {
        when (event) {
            is ExerciseTemplateEditorEvent.InitializeExerciseTemplate -> {
                initializeExerciseTemplate()
            }

            is ExerciseTemplateEditorEvent.CancelCreate -> {
                cancelCreate()
            }

            is ExerciseTemplateEditorEvent.UpdateExerciseMuscle -> {
                updateExerciseMuscle(event.exerciseMuscle)
            }

            is ExerciseTemplateEditorEvent.UpdateExerciseResistance -> {
                updateExerciseResistance(event.exerciseResistance)
            }

            is ExerciseTemplateEditorEvent.UpdateName -> {
                updateName(event.name)
            }
        }
    }

    private fun initializeExerciseTemplate() {
        viewModelScope.launch {
            when (val resource = exerciseTemplateUseCases.initializeExerciseTemplate()) {
                is Resource.Success -> {
                    val exerciseTemplateId = resource.data.toInt()
                    collectLatestExerciseTemplate(exerciseTemplateId)
                }

                is Resource.Error -> {
                    _exerciseTemplateState.update {
                        it.copy(
                            error = resource.errorMessage ?: "Error Initializing Exercise Template"
                        )
                    }
                }
            }
        }
    }

    private fun collectLatestExerciseTemplate(exerciseTemplateId: Int) {
        viewModelScope.launch {
            exerciseTemplateUseCases.getExerciseTemplateById(exerciseTemplateId)
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val exerciseTemplate = resource.data
                            exerciseTemplate?.let {
                                _exerciseTemplateState.update { it.copy(exerciseTemplate = exerciseTemplate) }
                            }
                        }

                        is Resource.Error -> {
                            _exerciseTemplateState.update {
                                it.copy(
                                    error = resource.errorMessage
                                        ?: "Error Retrieving Exercise Template"
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun cancelCreate() {
        val exerciseTemplateId = exerciseTemplateState.value.exerciseTemplate?.id
        if (exerciseTemplateId == null) {
            _exerciseTemplateState.update { it.copy(error = "Exercise Template is null in cancelCreate") }
            return
        }
        viewModelScope.launch {
            val resource = exerciseTemplateUseCases.discardInitializedTemplate(exerciseTemplateId)
            if (resource is Resource.Error) {
                _exerciseTemplateState.update {
                    it.copy(
                        error = resource.errorMessage
                            ?: "Error Discarding Initialized Exercise Template"
                    )
                }
            }
        }
    }

    private fun updateExerciseMuscle(exerciseMuscle: ExerciseMuscle) {
        val currentExerciseTemplate = exerciseTemplateState.value.exerciseTemplate ?: return
        viewModelScope.launch {
            if (exerciseMuscle != currentExerciseTemplate.exerciseMuscle) {
                exerciseTemplateUseCases.editExerciseTemplate(
                    currentExerciseTemplate.copy(
                        exerciseMuscle = exerciseMuscle,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private fun updateExerciseResistance(exerciseResistance: ExerciseResistance) {
        val currentExerciseTemplate = exerciseTemplateState.value.exerciseTemplate ?: return
        viewModelScope.launch {
            if (exerciseResistance != currentExerciseTemplate.exerciseResistance) {
                exerciseTemplateUseCases.editExerciseTemplate(
                    currentExerciseTemplate.copy(
                        exerciseResistance = exerciseResistance,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private fun updateName(name: String) {
        val currentExerciseTemplate = exerciseTemplateState.value.exerciseTemplate ?: return
        viewModelScope.launch {
            if (name != currentExerciseTemplate.name) {
                exerciseTemplateUseCases.editExerciseTemplate(
                    currentExerciseTemplate.copy(
                        name = name, updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }
}