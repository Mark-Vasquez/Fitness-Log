package com.example.fitnesslog.core.utils

fun secondsToMinutesAndSeconds(seconds: Int): Pair<Int, Int> {
    val minutes = (seconds / 60)
    val seconds = (seconds % 60)
    return Pair(minutes, seconds)
}