package com.example.fitnesslog.program.domain.use_case

/**
 * Creates a collection of UseCases object in the ProgramModuleImpl that have specific useCases as the properties
 * of this object
 * Use cases are called based on which UI event the ViewModel directs it to. This is where business logic
 * like validation and processing of data happens and then communicates with the repository instance
 * Prevents logic duplication in ViewModel if multiple vm's need to do same logic
 */
data class ProgramUseCases(
    val initializeProgram: InitializeProgram,
    val getPrograms: GetPrograms,
    val getProgram: GetProgram,
    val editProgram: EditProgram,
    val deleteProgram: DeleteProgram,
    val selectProgram: SelectProgram,
    val checkIfDeletable: CheckIfDeletable
)
