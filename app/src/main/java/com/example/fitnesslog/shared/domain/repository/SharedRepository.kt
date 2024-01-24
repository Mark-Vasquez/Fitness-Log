package com.example.fitnesslog.shared.domain.repository

import com.example.fitnesslog.core.utils.Resource

interface SharedRepository {

    suspend fun seedDatabaseIfFirstRun(): Resource<Unit>
}