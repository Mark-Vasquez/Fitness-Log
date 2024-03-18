package com.example.fitnesslog.core.utils.ui

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showSwipeDeleteDialog(
    context: Context,
    title: String,
    deleteConfirmation: String,
    onCancel: (() -> Unit),
    onDiscard: (() -> Unit),
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(deleteConfirmation)
        .setNegativeButton("Cancel") { _, _ ->
            onCancel()
        }
        .setPositiveButton("Delete") { _, _ ->
            onDiscard()
        }
        .show()
}