package com.example.fitnesslog.program.domain.use_case

/**
 * Creates an aggregate UseCases object in the ProgramModuleImpl that have specific useCases as the properties
 * of this object
 */
data class ProgramUseCases(
    val createProgram: CreateProgram,
    val getPrograms: GetPrograms,
    val editProgram: EditProgram,
    val deleteProgram: DeleteProgram,
    val selectProgram: SelectProgram,
    val seedProgram: SeedProgram
)
