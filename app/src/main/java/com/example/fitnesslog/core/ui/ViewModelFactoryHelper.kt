package com.example.fitnesslog.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified VM : ViewModel> viewModelFactory(crossinline creator: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VM::class.java)) {
                return creator() as T
            }
            throw IllegalArgumentException("ViewModel passed in the Provider does not match ViewModel configured in the Factory")
        }
    }
}
