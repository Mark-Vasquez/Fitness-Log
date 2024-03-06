package com.example.fitnesslog.ui.program.exercise_templates.exercise_template_editor

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.ExerciseMuscle
import com.example.fitnesslog.core.utils.constants.EXERCISE_MUSCLE
import com.example.fitnesslog.databinding.DialogExerciseMuscleSelectBinding

class ExerciseMuscleSelectDialog : DialogFragment() {
    private var _binding: DialogExerciseMuscleSelectBinding? = null
    private val binding: DialogExerciseMuscleSelectBinding get() = _binding!!
    private val args: ExerciseMuscleSelectDialogArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { context ->
            _binding = DialogExerciseMuscleSelectBinding.inflate(layoutInflater)

            if (savedInstanceState == null) {
                binding.apply {
                    cgMuscleSelect.clearCheck()
                    when (args.exerciseMuscle) {
                        ExerciseMuscle.ABS -> chipSelectAbs.isChecked = true
                        ExerciseMuscle.ARMS -> chipSelectArms.isChecked = true
                        ExerciseMuscle.BACK -> chipSelectBack.isChecked = true
                        ExerciseMuscle.CHEST -> chipSelectChest.isChecked = true
                        ExerciseMuscle.LEGS -> chipSelectLegs.isChecked = true
                        ExerciseMuscle.SHOULDERS -> chipSelectShoulders.isChecked = true
                    }
                }
            }

            val builder = AlertDialog.Builder(context)
            builder.setView(binding.root)
                .setPositiveButton("OK") { _, _ ->
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        EXERCISE_MUSCLE,
                        // Pass an enum back based on what single chip was checked
                        getSelectedMuscle()
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

    private fun getSelectedMuscle(): ExerciseMuscle {
        return when (binding.cgMuscleSelect.checkedChipId) {
            R.id.chipSelectAbs -> ExerciseMuscle.ABS
            R.id.chipSelectArms -> ExerciseMuscle.ARMS
            R.id.chipSelectBack -> ExerciseMuscle.BACK
            R.id.chipSelectChest -> ExerciseMuscle.CHEST
            R.id.chipSelectLegs -> ExerciseMuscle.LEGS
            R.id.chipSelectShoulders -> ExerciseMuscle.SHOULDERS
            else -> {
                throw IllegalStateException("No chip selected")
            }
        }
    }
}