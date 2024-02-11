package com.example.fitnesslog.domain.use_case.shared

import androidx.room.Transaction
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.domain.repository.SharedRepository

class SeedInitialApplication(
    private val sharedRepository: SharedRepository
) {
    @Transaction
    suspend operator fun invoke(): Resource<Unit> {
        return sharedRepository.seedDatabaseIfFirstRun()
    }
}