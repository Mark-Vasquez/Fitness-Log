package com.example.fitnesslog.core.utils

suspend fun <T> safeCall(call: suspend () -> T): Resource<T> {
    return try {
        Resource.Success(call())
    } catch (e: Exception) {
        Resource.Error(e.toErrorMessage())
    }
}