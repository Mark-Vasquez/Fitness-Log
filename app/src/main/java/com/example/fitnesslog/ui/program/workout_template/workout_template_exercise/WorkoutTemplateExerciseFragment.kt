package com.example.fitnesslog.ui.program.workout_template.workout_template_exercise

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
import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.databinding.FragmentWorkoutTemplateExerciseBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class WorkoutTemplateExerciseFragment : Fragment() {
    private val workoutTemplateExerciseViewModel: WorkoutTemplateExerciseViewModel by viewModels {
        WorkoutTemplateExerciseViewModel.Factory(
            args.workoutTemplateExerciseId,
            appModule.workoutTemplateUseCases
        )
    }
    private var _binding: FragmentWorkoutTemplateExerciseBinding? = null
    private val binding: FragmentWorkoutTemplateExerciseBinding get() = _binding!!
    private val args: WorkoutTemplateExerciseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutTemplateExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeWorkoutTemplateExerciseState()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeWorkoutTemplateExerciseState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                workoutTemplateExerciseViewModel.workoutTemplateExerciseState.collectLatest {
                    it.workoutTemplateExercise?.let { workoutTemplateExercise ->
                        binding.tvExerciseNameTitle.text = workoutTemplateExercise.name
                    }
                }
            }
        }
    }
}