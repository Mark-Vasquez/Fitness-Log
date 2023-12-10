package com.example.fitnesslog.core.utils

import android.content.Context
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showDiscardDialog(
    context: Context,
    parentDialog: DialogFragment,
    onDismissDialog: (() -> Unit)? = null
) {
    MaterialAlertDialogBuilder(context)
        .setTitle("Unsaved Changes")
        .setMessage("Are you sure you want to discard changes?")
        .setNegativeButton("Cancel", null)
        .setPositiveButton("Discard") { _, _ ->
            if (parentDialog.isAdded) {
                parentDialog.dismiss()
            }
        }
        .setOnDismissListener {
            onDismissDialog?.invoke()
        }
        .show()
}