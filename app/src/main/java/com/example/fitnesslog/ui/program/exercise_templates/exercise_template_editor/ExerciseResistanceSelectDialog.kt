package com.example.fitnesslog.ui.program.exercise_templates.exercise_template_editor

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.fitnesslog.databinding.DialogExerciseResistanceSelectBinding

class ExerciseResistanceSelectDialog : DialogFragment() {
    private var _binding: DialogExerciseResistanceSelectBinding? = null
    private val binding: DialogExerciseResistanceSelectBinding get() = _binding!!
    private val args: ExerciseResistanceSelectDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { context ->
            _binding = DialogExerciseResistanceSelectBinding.inflate(layoutInflater)

            val builder = AlertDialog.Builder(context)
            builder.setView(binding.root)
            builder.create() as Dialog
        } ?: throw IllegalStateException("Activity cannot be null when calling onCreateDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}