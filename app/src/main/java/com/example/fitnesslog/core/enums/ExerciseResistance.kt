package com.example.fitnesslog.core.enums

enum class ExerciseResistance {
    BARBELL,
    BODY_WEIGHT,
    DUMBBELL,
    MACHINE,
    OTHER;

    val displayName: String
        get() = this.name
            .lowercase()
            .split("_")
            .joinToString(" ") { it.replaceFirstChar { char -> char.titlecase() } }
}