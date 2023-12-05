package com.example.fitnesslog.core.utils

import android.content.Context
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showDiscardDialog(context: Context, dialog: DialogFragment) {
    MaterialAlertDialogBuilder(context)
        .setTitle("Unsaved Changes")
        .setMessage("Are you sure you want to discard changes?")
        .setNegativeButton("Cancel", null)
        .setPositiveButton("Discard") { _, _ ->
            if (dialog.isAdded) {
                dialog.dismiss()
            }

        }
        .show()
}