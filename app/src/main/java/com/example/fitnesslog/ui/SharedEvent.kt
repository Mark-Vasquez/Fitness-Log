package com.example.fitnesslog.ui

sealed class SharedEvent {
    data object ClearErrorState : SharedEvent()
}
