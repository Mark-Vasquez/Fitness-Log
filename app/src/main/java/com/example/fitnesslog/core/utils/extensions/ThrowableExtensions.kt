package com.example.fitnesslog.core.utils.extensions

fun Throwable.toErrorMessage(): String {
    return "An error occurred: ${this.localizedMessage ?: "An unknown error occurred."}"
}