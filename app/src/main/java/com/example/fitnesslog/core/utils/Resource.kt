package com.example.fitnesslog.core.utils

sealed class Resource<T>(open val data: T? = null, val errorMessage: String? = null) {
    class Success<T>(override val data: T) : Resource<T>(data)
    class Error<T>(errorMessage: String, data: T? = null) : Resource<T>(data, errorMessage)
}
