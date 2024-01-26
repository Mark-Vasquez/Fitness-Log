package com.example.fitnesslog.program.ui.program

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.FitnessLogApp.Companion.workoutTemplateModule
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.use_case.program.ProgramUseCases
import com.example.fitnesslog.program.domain.use_case.workout_template.WorkoutTemplateUseCases
import com.example.fitnesslog.program.ui.ProgramMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgramViewModel(
    private val programUseCases: ProgramUseCases,
    private val workoutTemplateUseCases: WorkoutTemplateUseCases
) : ViewModel() {

    private val _programState = MutableStateFlow(ProgramState())
    val programState = _programState.asStateFlow()

    private val _workoutTemplatesState = MutableStateFlow(WorkoutTemplatesState())
    val workoutTemplatesState = _workoutTemplatesState.asStateFlow()

    companion object {
        const val TAG = "ProgramViewModel"
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProgramViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ProgramViewModel(
                        programModule.programUseCases,
                        workoutTemplateModule.workoutTemplateUseCases
                    ) as T
                }
                throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
            }

        }
    }


    fun onEvent(event: ProgramEvent) {
        when (event) {
            is ProgramEvent.CreateMode -> {
                if (programState.value.program == null) {
                    // Only initialize once, not again on rotate when onCreate is called again
                    initializeProgram()
                    _programState.value = programState.value.copy(programMode = event.mode)
                }
            }

            is ProgramEvent.EditMode -> {
                if (programState.value.program == null) {
                    getProgram(event.programId)
                    checkIfDeletable()
                    _programState.value = programState.value.copy(programMode = event.mode)

                }
            }

            is ProgramEvent.Save -> {
                saveProgram()
            }

            is ProgramEvent.Cancel -> {
                when (programState.value.programMode) {
                    ProgramMode.CREATE -> cancelCreate()
                    ProgramMode.EDIT -> return
                }
            }

            is ProgramEvent.UpdateName -> {
                updateName(event.name)
            }

            is ProgramEvent.UpdateScheduledDays -> {
                updateScheduledDays(event.scheduledDays)
            }

            is ProgramEvent.UpdateRestDurationSeconds -> {
                updateRestDurationSeconds(event.restDurationSeconds)
            }


            is ProgramEvent.Delete -> {
                deleteProgram()
            }
        }
    }

    private fun collectLatestWorkoutTemplates() {
        val programId = programState.value.program?.id ?: return
        viewModelScope.launch {
            workoutTemplateUseCases.getWorkoutTemplates(programId).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val workoutTemplates = resource.data
                        _workoutTemplatesState.update { it.copy(workoutTemplates = workoutTemplates) }
                    }

                    is Resource.Error -> {
                        _workoutTemplatesState.update { it.copy(error = resource.errorMessage) }
                    }
                }
            }
        }
    }

    private fun initializeProgram() {
        viewModelScope.launch {
            when (val resource = programUseCases.initializeProgram()) {
                is Resource.Success -> {
                    val programId = resource.data.toInt()
                    getProgram(programId)
                }

                is Resource.Error -> {
                    _programState.value = programState.value.copy(
                        error = resource.errorMessage
                            ?: "Error Initializing Program in `$TAG`"
                    )
                }
            }
        }
    }

    private fun updateName(name: String) {
        _programState.value = programState.value.copy(
            name = name,
        )
    }

    private fun updateScheduledDays(scheduledDays: Set<Day>) {
        _programState.value = programState.value.copy(
            scheduledDays = scheduledDays
        )
    }

    private fun updateRestDurationSeconds(restDurationSeconds: Int) {
        _programState.value = programState.value.copy(
            restDurationSeconds = restDurationSeconds
        )
    }


    private fun getProgram(programId: Int) {
        viewModelScope.launch {
            when (val resource = programUseCases.getProgram(programId)) {
                is Resource.Success -> {
                    val program = resource.data
                    _programState.value = programState.value.copy(
                        program = program,
                        name = program.name,
                        scheduledDays = program.scheduledDays,
                        restDurationSeconds = program.restDurationSeconds
                    )
                }

                is Resource.Error -> {
                    _programState.value = programState.value.copy(
                        error = resource.errorMessage ?: "Error Retrieving Program"
                    )
                }
            }

        }
    }

    private fun checkIfDeletable() {
        viewModelScope.launch {
            when (val resource = programUseCases.checkIfDeletable()) {
                is Resource.Success -> {
                    _programState.value =
                        programState.value.copy(isDeletable = resource.data)
                }

                is Resource.Error -> {
                    _programState.value = programState.value.copy(
                        error = resource.errorMessage ?: "Error checking if deletable"
                    )
                }
            }
        }
    }

    private fun deleteProgram() {
        val programId = programState.value.program?.id ?: return
        viewModelScope.launch {
            programUseCases.deleteProgram(programId)
        }
    }

    // Calls an edit to the already initialized Program with the user inputs
    private fun saveProgram() {
        val oldProgram = programState.value.program
        if (oldProgram == null) {
            _programState.value =
                programState.value.copy(error = "Error Saving `null` Program in `save`")
            return
        }
        val newProgram = oldProgram.copy(
            name = programState.value.name,
            scheduledDays = programState.value.scheduledDays,
            restDurationSeconds = programState.value.restDurationSeconds,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            val resource = programUseCases.editProgram(newProgram)
            if (resource is Resource.Error) {
                _programState.value = programState.value.copy(
                    error = resource.errorMessage ?: "Saving changes failed"
                )

            }

        }
    }

    // Deletes the already initialized program instance along with associated workouts
    private fun cancelCreate() {
        val programId = programState.value.program?.id
        if (programId == null) {
            _programState.value =
                programState.value.copy(error = "Program is null in `cancelCreate`")
            return
        }
        viewModelScope.launch {
            when (val resource = programUseCases.deleteProgram(programId)) {
                is Resource.Success -> {
                }

                is Resource.Error -> {
                    _programState.value = programState.value.copy(
                        error = resource.errorMessage
                            ?: "Error Discarding Program in `cancelCreate`"
                    )
                }
            }
        }
    }


}