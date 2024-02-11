package com.example.fitnesslog.domain.repository

import com.example.fitnesslog.core.utils.Resource

interface SharedRepository {

    suspend fun seedDatabaseIfFirstRun(): Resource<Unit>
}