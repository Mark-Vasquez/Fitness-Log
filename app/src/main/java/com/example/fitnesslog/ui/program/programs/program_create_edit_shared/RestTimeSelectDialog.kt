package com.example.fitnesslog.ui.program.programs.program_create_edit_shared

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitnesslog.core.utils.constants.REST_DURATION_SECONDS
import com.example.fitnesslog.core.utils.helpers.minutesAndSecondsToSeconds
import com.example.fitnesslog.core.utils.helpers.secondsToMinutesAndSeconds
import com.example.fitnesslog.databinding.DialogRestTimeSelectBinding

class RestTimeSelectDialog : DialogFragment() {

    private var _binding: DialogRestTimeSelectBinding? = null
    private val binding get() = _binding!!
    private val args: RestTimeSelectDialogArgs by navArgs()


    companion object {
        const val TAG = "RestTimeSelectDialog"
        const val MINUTE = "minute"
        const val SECOND = "second"
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            _binding = DialogRestTimeSelectBinding.inflate(layoutInflater)

            val restoredMinutes: Int
            val restoredSeconds: Int

            if (savedInstanceState == null) {
                restoredMinutes = secondsToMinutesAndSeconds(args.restDurationSeconds).first
                restoredSeconds = secondsToMinutesAndSeconds(args.restDurationSeconds).second
            } else {
                restoredMinutes = savedInstanceState.getInt(MINUTE)
                restoredSeconds = savedInstanceState.getInt(SECOND)
            }


            binding.npMinuteTimeSelect.apply {
                minValue = 0
                maxValue = 10
                value = restoredMinutes
            }
            binding.npSecondTimeSelect.apply {
                minValue = 0
                maxValue = 59
                value = restoredSeconds
            }

            val builder = AlertDialog.Builder(it)
            builder.setView(binding.root)
                .setPositiveButton("OK") { _, _ ->
                    // pass the duration that the user set, back to the parent fragment
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        REST_DURATION_SECONDS,
                        minutesAndSecondsToSeconds(
                            binding.npMinuteTimeSelect.value,
                            binding.npSecondTimeSelect.value
                        )
                    )
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    // do not update the time
                    dialog.cancel()
                }
            builder.create() as Dialog

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save user input state when rotating
        outState.putInt(MINUTE, binding.npMinuteTimeSelect.value)
        outState.putInt(SECOND, binding.npSecondTimeSelect.value)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
