package com.example.fitnesslog.ui.program.exercise_templates.default_exercise_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.use_case.exercise_template.ExerciseTemplateUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DefaultExerciseInfoViewModel(
    exerciseTemplateId: Int,
    private val exerciseTemplateUseCases: ExerciseTemplateUseCases
) : ViewModel() {

    private val _exerciseInfoState = MutableStateFlow(ExerciseInfoState())
    val exerciseInfoState = _exerciseInfoState.asStateFlow()

    class Factory(
        private val exerciseTemplateId: Int,
        private val exerciseTemplateUseCases: ExerciseTemplateUseCases
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DefaultExerciseInfoViewModel::class.java)) {
                return DefaultExerciseInfoViewModel(
                    exerciseTemplateId,
                    exerciseTemplateUseCases
                ) as T
            }
            throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
        }
    }

    init {
        collectLatestExerciseTemplate(exerciseTemplateId)
    }

    private fun collectLatestExerciseTemplate(exerciseTemplateId: Int) {
        viewModelScope.launch {
            exerciseTemplateUseCases.getExerciseTemplateById(exerciseTemplateId)
                .collectLatest { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            val exerciseTemplate = resource.data
                            exerciseTemplate?.let {
                                _exerciseInfoState.update { it.copy(exerciseTemplate = exerciseTemplate) }
                            }
                        }

                        is Resource.Error -> {

                        }
                    }
                }
        }
    }
}