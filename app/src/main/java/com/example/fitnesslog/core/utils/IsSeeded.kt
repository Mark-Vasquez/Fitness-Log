package com.example.fitnesslog.core.utils

import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import kotlinx.coroutines.flow.first

suspend fun isSeeded(): Boolean {
    val preferences = appModule.dataStore.data.first()
    return preferences[IS_SEEDED] ?: false
}