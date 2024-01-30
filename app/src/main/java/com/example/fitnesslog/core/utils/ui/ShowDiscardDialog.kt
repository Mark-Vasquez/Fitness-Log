package com.example.fitnesslog.core.utils.ui

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showDiscardDialog(
    context: Context,
    onDiscard: (() -> Unit)
) {
    MaterialAlertDialogBuilder(context)
        .setTitle("Unsaved Changes")
        .setMessage("Are you sure you want to discard changes?")
        .setNegativeButton("Cancel", null)
        .setPositiveButton("Discard") { _, _ ->
            onDiscard()
        }
        .show()
}