package com.example.fitnesslog.program.domain.use_case

/**
 * Creates an aggregate UseCases object in the ProgramModuleImpl that have specific useCases as the properties
 * of this object
 * Use cases are called based on which UI event the ViewModel directs it to. This is where business logic
 * like validation and processing of data happens and then communicates with the repository instance
 * Prevents logic duplication in ViewModel
 */
data class ProgramUseCases(
    val createProgramWithDefaultValues: CreateProgramWithDefaultValues,
    val getPrograms: GetPrograms,
    val editProgram: EditProgram,
    val deleteProgram: DeleteProgram,
    val selectProgram: SelectProgram,
    val seedProgram: SeedProgram
)
