package com.example.fitnesslog.core.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showDeleteDialog(
    context: Context,
    deleteConfirmation: String,
    onDiscard: (() -> Unit)
) {
    MaterialAlertDialogBuilder(context)
        .setTitle("Delete Program")
        .setMessage(deleteConfirmation)
        .setNegativeButton("Cancel", null)
        .setPositiveButton("Delete") { _, _ ->
            onDiscard()
        }
        .show()
}