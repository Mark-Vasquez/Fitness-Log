package com.example.fitnesslog.ui.shared

sealed class SharedEvent {
    data object ClearErrorState : SharedEvent()
}
