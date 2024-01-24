package com.example.fitnesslog.program.domain.use_case

import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.program.domain.repository.ProgramRepository

class CheckIfDeletable(
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke(): Resource<Boolean> {
        when (val resource = programRepository.getProgramsCount()) {
            is Resource.Success -> {
                val programCount = resource.data
                if (programCount > 1) {
                    return Resource.Success(true)
                } else {
                    return Resource.Success(false)
                }
            }

            is Resource.Error -> {
                return Resource.Error(
                    resource.errorMessage ?: "Unable to check Program count for deletion"
                )
            }
        }
    }
}
