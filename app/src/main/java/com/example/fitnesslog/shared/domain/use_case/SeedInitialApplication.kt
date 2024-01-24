package com.example.fitnesslog.shared.domain.use_case

import androidx.room.Transaction
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.shared.domain.repository.SharedRepository

class SeedInitialApplication(
    private val sharedRepository: SharedRepository
) {
    @Transaction
    suspend operator fun invoke(): Resource<Unit> {
        return sharedRepository.seedDatabaseIfFirstRun()
    }
}