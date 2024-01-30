package com.example.fitnesslog.core.utils

import com.example.fitnesslog.core.utils.extensions.toErrorMessage

suspend fun <T> safeCall(call: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(data = call())
    } catch (e: Exception) {
        Resource.Error(errorMessage = e.toErrorMessage())
    }
}