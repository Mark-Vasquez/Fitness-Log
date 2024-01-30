package com.example.fitnesslog.core.utils.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Calling this extended method on an EditText instance adds a custom TextWatcher as a listener to it.
 * When the listener methods are triggered, `trySend` function sends the callback parameter value (the string)
 * into the callbackFlow's internal queue-like channel. This value is emitted as a flow to be collected
 */
fun EditText.textChangeFlow(): Flow<String> = callbackFlow {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                trySend(s.toString()) // sends the string into the internal channel of callbackFlow
            }
        }
    }

    addTextChangedListener(textWatcher)
    // Suspends the emitting callbackFlow coroutine until collecting coroutine is cancelled or emitting is complete,
    // then removes the textWatcher to avoid potential memory leaks
    awaitClose { removeTextChangedListener(textWatcher) }
}