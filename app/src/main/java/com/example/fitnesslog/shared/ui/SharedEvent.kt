package com.example.fitnesslog.shared.ui

sealed class SharedEvent {
    data object ClearErrorState : SharedEvent()
}
