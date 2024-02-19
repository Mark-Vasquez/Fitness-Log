package com.example.fitnesslog.ui.program.exercise_templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitnesslog.databinding.FragmentExerciseTemplatesBinding

class ExerciseTemplatesFragment : Fragment() {
    private var _binding: FragmentExerciseTemplatesBinding? = null
    private val binding: FragmentExerciseTemplatesBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseTemplatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}