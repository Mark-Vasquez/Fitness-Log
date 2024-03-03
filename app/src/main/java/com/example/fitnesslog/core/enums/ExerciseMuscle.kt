package com.example.fitnesslog.core.enums

enum class ExerciseMuscle {
    ABS,
    ARMS,
    BACK,
    CHEST,
    LEGS,
    SHOULDERS;

    val displayName: String
        get() = this.name
            .lowercase()
            .split("_")
            .joinToString(" ") { it.replaceFirstChar { char -> char.titlecase() } }
}