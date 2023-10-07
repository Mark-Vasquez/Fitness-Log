package com.example.fitnesslog.program.domain.use_case

data class ProgramUseCases(
    val createProgramUseCase: CreateProgramUseCase,
    val getProgramUseCase: GetProgramsUseCase,
    val editProgramUseCase: EditProgramUseCase,
    val deleteProgramUseCase: DeleteProgramUseCase,
    val selectProgramUseCase: SelectProgramUseCase
)
