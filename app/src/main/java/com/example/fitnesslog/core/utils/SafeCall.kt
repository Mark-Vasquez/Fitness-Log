package com.example.fitnesslog.core.utils

suspend fun <T> safeCall(call: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(data = call())
    } catch (e: Exception) {
        Resource.Error(errorMessage = e.toErrorMessage())
    }
}