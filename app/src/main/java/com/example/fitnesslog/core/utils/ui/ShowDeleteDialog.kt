package com.example.fitnesslog.core.utils.ui

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showDeleteDialog(
    context: Context,
    title: String,
    deleteConfirmation: String,
    onDiscard: (() -> Unit)
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(deleteConfirmation)
        .setNegativeButton("Cancel", null)
        .setPositiveButton("Delete") { _, _ ->
            onDiscard()
        }
        .show()
}