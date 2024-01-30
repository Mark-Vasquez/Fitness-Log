package com.example.fitnesslog.program.ui.program

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
    private lateinit var minutesAndSeconds: Pair<Int, Int>

    companion object {
        const val TAG = "RestTimeSelectDialog"
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            minutesAndSeconds = secondsToMinutesAndSeconds(args.restDurationSeconds)

            _binding = DialogRestTimeSelectBinding.inflate(layoutInflater)
            binding.npMinuteTimeSelect.apply {
                minValue = 0
                maxValue = 10
                value = minutesAndSeconds.first
            }
            binding.npSecondTimeSelect.apply {
                minValue = 0
                maxValue = 59
                value = minutesAndSeconds.second
            }
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

            // What happens if you set view values here?
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
