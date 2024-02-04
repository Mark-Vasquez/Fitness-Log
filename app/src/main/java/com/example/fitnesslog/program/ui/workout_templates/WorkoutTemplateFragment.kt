package com.example.fitnesslog.program.ui.workout_templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.fitnesslog.FitnessLogApp.Companion.workoutTemplateModule
import com.example.fitnesslog.databinding.FragmentWorkoutTemplateBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WorkoutTemplateFragment : Fragment() {
    private val workoutTemplateViewModel: WorkoutTemplateViewModel by viewModels {
        WorkoutTemplateViewModel.Factory(
            args.workoutTemplateId,
            workoutTemplateModule.workoutTemplateUseCases
        )
    }

    private var _binding: FragmentWorkoutTemplateBinding? = null
    private val binding: FragmentWorkoutTemplateBinding get() = _binding!!
    private val args: WorkoutTemplateFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeWorkoutTemplateState()
    }

    private fun observeWorkoutTemplateState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                workoutTemplateViewModel.workoutTemplateState.collectLatest {
                    it.workoutTemplate?.let { workoutTemplate ->
                        updateNameInputView(workoutTemplate.name)
                    }
                }
            }
        }
    }

    private fun updateNameInputView(name: String) {
        if (binding.etNameWorkoutTemplate.text.toString() != name) {
            binding.etNameWorkoutTemplate.setText(name)
            binding.etNameWorkoutTemplate.setSelection(binding.etNameWorkoutTemplate.length())
        }
    }
}