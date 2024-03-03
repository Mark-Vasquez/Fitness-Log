package com.example.fitnesslog.ui.program.programs.program_create_edit_shared.program_create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.data.entity.WorkoutTemplate
import com.example.fitnesslog.ui.program.programs.program_create_edit_shared.WorkoutTemplatesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgramCreateViewModel(
    private val programUseCases: com.example.fitnesslog.domain.use_case.program.ProgramUseCases,
    private val workoutTemplateUseCases: com.example.fitnesslog.domain.use_case.workout_template.WorkoutTemplateUseCases
) : ViewModel() {

    private val _programState = MutableStateFlow(ProgramCreateState())
    val programState = _programState.asStateFlow()

    private val _workoutTemplatesState = MutableStateFlow(WorkoutTemplatesState())
    val workoutTemplatesState = _workoutTemplatesState.asStateFlow()

    companion object {
        const val TAG = "ProgramCreateViewModel"
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProgramCreateViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return ProgramCreateViewModel(
                        appModule.programUseCases,
                        appModule.workoutTemplateUseCases
                    ) as T
                }
                throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
            }

        }
    }

    init {
        initializeProgram()
    }


    fun onEvent(event: ProgramCreateEvent) {
        when (event) {

            is ProgramCreateEvent.Save -> {
                saveProgram()
            }

            is ProgramCreateEvent.Cancel -> {
                cancelCreate()
            }

            is ProgramCreateEvent.UpdateName -> {
                updateName(event.name)
            }

            is ProgramCreateEvent.UpdateScheduledDays -> {
                updateScheduledDays(event.scheduledDays)
            }

            is ProgramCreateEvent.UpdateRestDurationSeconds -> {
                updateRestDurationSeconds(event.restDurationSeconds)
            }

            is ProgramCreateEvent.UpdateWorkoutTemplatesOrder -> {
                updateWorkoutTemplatesOrder(event.workoutTemplates)
            }

            is ProgramCreateEvent.CreateWorkoutTemplate -> {
                createWorkoutTemplate()
            }

        }
    }

    private fun initializeProgram() {
        viewModelScope.launch {
            when (val resource = programUseCases.initializeProgram()) {
                is Resource.Success -> {
                    val programId = resource.data.toInt()
                    getProgram(programId)
                    collectLatestWorkoutTemplatesByProgramId(programId)
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

    private fun getProgram(programId: Int) {
        viewModelScope.launch {
            programUseCases.getProgram(programId).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val program = resource.data
                        program?.let {
                            _programState.value = programState.value.copy(
                                program = program,
                                name = program.name,
                                scheduledDays = program.scheduledDays,
                                restDurationSeconds = program.restDurationSeconds
                            )
                        }
                    }

                    is Resource.Error -> {
                        _programState.value = programState.value.copy(
                            error = resource.errorMessage ?: "Error Retrieving Program"
                        )
                    }
                }
            }

        }
    }

    private suspend fun collectLatestWorkoutTemplatesByProgramId(programId: Int) {
        workoutTemplateUseCases.getWorkoutTemplates(programId)
            .collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> _workoutTemplatesState.update {
                        it.copy(workoutTemplates = resource.data)
                    }

                    is Resource.Error -> _programState.update { it.copy(error = resource.errorMessage) }
                }
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
        val editedProgram = oldProgram.copy(
            name = programState.value.name,
            scheduledDays = programState.value.scheduledDays,
            restDurationSeconds = programState.value.restDurationSeconds,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            val resource = programUseCases.editProgram(editedProgram)
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

    private fun updateName(name: String) {
        _programState.value = programState.value.copy(
            name = name
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

    private fun updateWorkoutTemplatesOrder(workoutTemplates: List<WorkoutTemplate>) {
        viewModelScope.launch {
            workoutTemplateUseCases.reorderWorkoutTemplates(workoutTemplates)
        }
    }

    private fun createWorkoutTemplate() {
        val programId = programState.value.program?.id ?: return
        viewModelScope.launch {
            workoutTemplateUseCases.createWorkoutTemplate(programId)
        }
    }


}