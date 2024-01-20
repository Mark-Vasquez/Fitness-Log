package com.example.fitnesslog.program.ui.program_create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.fitnesslog.databinding.DialogRestTimeSelectBinding

class RestTimeSelectDialog : DialogFragment() {

    private var _binding: DialogRestTimeSelectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogRestTimeSelectBinding.inflate(inflater, container, false)
        return binding.root
    }
}