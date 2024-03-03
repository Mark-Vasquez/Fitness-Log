package com.example.fitnesslog.ui.program.exercise_templates.default_exercise_info

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
import com.example.fitnesslog.databinding.FragmentDefaultExerciseInfoBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DefaultExerciseInfoFragment : Fragment() {
    private val defaultExerciseInfoViewModel: DefaultExerciseInfoViewModel by viewModels {
        DefaultExerciseInfoViewModel.Factory(
            args.exerciseTemplateId,
            appModule.exerciseTemplateUseCases
        )
    }

    private var _binding: FragmentDefaultExerciseInfoBinding? = null
    private val binding: FragmentDefaultExerciseInfoBinding get() = _binding!!
    private val args: DefaultExerciseInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDefaultExerciseInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeExerciseInfoState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeExerciseInfoState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                defaultExerciseInfoViewModel.exerciseInfoState.collectLatest {
                    it.exerciseTemplate?.let { exerciseTemplate ->
                        binding.apply {
                            tvMuscleType.text = exerciseTemplate.exerciseMuscle.displayName
                            tvResistanceType.text = exerciseTemplate.exerciseResistance.displayName
                        }
                    }
                }
            }
        }
    }
}