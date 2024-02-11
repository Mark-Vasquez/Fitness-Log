package com.example.fitnesslog.domain.use_case.program

/**
 * Creates a collection of UseCases object in the ProgramModuleImpl that have specific useCases as the properties
 * of this object
 * Use cases are called based on which UI event the ViewModel directs it to. This is where business logic
 * like validation and processing of data happens and then communicates with the repository instance
 * Prevents logic duplication in ViewModel if multiple vm's need to do same logic
 */
data class ProgramUseCases(
    val initializeProgram: com.example.fitnesslog.domain.use_case.program.InitializeProgram,
    val getPrograms: com.example.fitnesslog.domain.use_case.program.GetPrograms,
    val getProgram: com.example.fitnesslog.domain.use_case.program.GetProgram,
    val editProgram: com.example.fitnesslog.domain.use_case.program.EditProgram,
    val deleteProgram: com.example.fitnesslog.domain.use_case.program.DeleteProgram,
    val selectProgram: com.example.fitnesslog.domain.use_case.program.SelectProgram,
    val checkIfDeletable: com.example.fitnesslog.domain.use_case.program.CheckIfDeletable
)
