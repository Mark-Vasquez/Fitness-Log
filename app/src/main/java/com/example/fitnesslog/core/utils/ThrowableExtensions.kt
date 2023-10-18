package com.example.fitnesslog.core.utils

fun Throwable.toErrorMessage(): String {
    return "An error occurred: ${this.localizedMessage ?: "An unknown error occurred."}"
}