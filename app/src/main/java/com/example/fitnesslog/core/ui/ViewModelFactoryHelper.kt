package com.example.fitnesslog.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/** Takes in a creator lambda that returns a ViewModel that has been injected with dependencies
 *  and returns an anonymous ViewModelFactory object
 *  That ViewModelFactory object has a create method that takes in a ViewModel type and checks if that passed in
 *  ViewModel type is the same type as the one returning from the creator lambda and then the create method calls
 *  and returns the creator lambda, which returns the ViewModel instance with injected dependencies
 */
inline fun <reified VM : ViewModel> viewModelFactoryHelper(crossinline creator: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VM::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return creator() as T
            }
            throw IllegalArgumentException("ViewModel type passed in the Provider does not match ViewModel configured in the Factory")
        }
    }
}
