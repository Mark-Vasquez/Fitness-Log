package com.example.fitnesslog.ui.program.exercise_templates.exercise_template_editor

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.ExerciseResistance
import com.example.fitnesslog.core.utils.constants.EXERCISE_RESISTANCE
import com.example.fitnesslog.databinding.DialogExerciseResistanceSelectBinding

class ExerciseResistanceSelectDialog : DialogFragment() {
    private var _binding: DialogExerciseResistanceSelectBinding? = null
    private val binding: DialogExerciseResistanceSelectBinding get() = _binding!!
    private val args: ExerciseResistanceSelectDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { context ->
            _binding = DialogExerciseResistanceSelectBinding.inflate(layoutInflater)

            if (savedInstanceState == null) {
                binding.apply {
                    cgResistanceSelect.clearCheck()
                    when (args.exerciseResistance) {
                        ExerciseResistance.BARBELL -> chipSelectBarbell.isChecked = true
                        ExerciseResistance.DUMBBELL -> chipSelectDumbbell.isChecked = true
                        ExerciseResistance.MACHINE -> chipSelectMachine.isChecked = true
                        ExerciseResistance.BODY_WEIGHT -> chipSelectBodyWeight.isChecked = true
                        ExerciseResistance.OTHER -> chipSelectOther.isChecked = true
                    }
                }
            }

            val builder = AlertDialog.Builder(context)
            builder.setView(binding.root)
                .setPositiveButton("OK") { _, _ ->
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        EXERCISE_RESISTANCE,
                        getSelectedResistance()
                    )
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }
            builder.create() as Dialog
        } ?: throw IllegalStateException("Activity cannot be null when calling onCreateDialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getSelectedResistance(): ExerciseResistance {
        return when (binding.cgResistanceSelect.checkedChipId) {
            R.id.chipSelectBarbell -> ExerciseResistance.BARBELL
            R.id.chipSelectDumbbell -> ExerciseResistance.DUMBBELL
            R.id.chipSelectMachine -> ExerciseResistance.MACHINE
            R.id.chipSelectBodyWeight -> ExerciseResistance.BODY_WEIGHT
            R.id.chipSelectOther -> ExerciseResistance.OTHER
            else -> {
                throw IllegalStateException("No chip selected")
            }
        }
    }
}