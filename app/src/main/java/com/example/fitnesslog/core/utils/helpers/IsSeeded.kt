package com.example.fitnesslog.core.utils.helpers

import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.core.utils.constants.IS_SEEDED
import kotlinx.coroutines.flow.first

suspend fun isSeeded(): Boolean {
    val preferences = appModule.dataStore.data.first()
    return preferences[IS_SEEDED] ?: false
}