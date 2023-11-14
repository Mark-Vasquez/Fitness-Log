package com.example.fitnesslog.program.domain.use_case

data class ProgramUseCases(
    val createProgram: CreateProgram,
    val getPrograms: GetPrograms,
    val editProgram: EditProgram,
    val deleteProgram: DeleteProgram,
    val selectProgram: SelectProgram,
    val seedProgram: SeedProgram
)
