package com.example.fitnesslog.domain.use_case.program

import com.example.fitnesslog.core.utils.Resource

class CheckIfDeletable(
    private val programRepository: com.example.fitnesslog.domain.repository.ProgramRepository
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
