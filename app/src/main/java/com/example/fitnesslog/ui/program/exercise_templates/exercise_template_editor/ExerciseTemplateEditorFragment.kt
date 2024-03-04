package com.example.fitnesslog.ui.program.exercise_templates.exercise_template_editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.EditorMode
import com.example.fitnesslog.core.utils.ui.showDiscardDialog
import com.example.fitnesslog.databinding.FragmentExerciseTemplateEditorBinding
import kotlinx.coroutines.launch

class ExerciseTemplateEditorFragment : Fragment() {
    private val exerciseTemplateEditorViewModel: ExerciseTemplateEditorViewModel by viewModels {
        ExerciseTemplateEditorViewModel.Factory(
            appModule.exerciseTemplateUseCases
        )
    }
    private var _binding: FragmentExerciseTemplateEditorBinding? = null
    private val binding: FragmentExerciseTemplateEditorBinding get() = _binding!!
    private val args: ExerciseTemplateEditorFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseTemplateEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (args.editorMode) {
            EditorMode.CREATE -> {
                initializeCreateMode()
                binding.btnNavigateBack.setOnClickListener {
                    showDiscardDialog(requireContext()) {
                        exerciseTemplateEditorViewModel.onEvent(ExerciseTemplateEditorEvent.CancelCreate)
                    }
                }
                binding.btnSaveExerciseTemplate.setOnClickListener {
                    findNavController().popBackStack()

                }
            }

            EditorMode.EDIT -> {
                binding.btnNavigateBack.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
        setupUI()
        observeExerciseTemplateState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeCreateMode() {
        exerciseTemplateEditorViewModel.onEvent(ExerciseTemplateEditorEvent.InitializeExerciseTemplate)
    }

    private fun setupUI() {
        when (args.editorMode) {
            EditorMode.CREATE -> {
                binding.tvTitleExerciseTemplate.text = getString(R.string.create_exercise)
                binding.btnNavigateBack.text = getString(R.string.cancel)
                binding.btnDeleteExercise.visibility = View.GONE
            }

            EditorMode.EDIT -> {
                binding.tvTitleExerciseTemplate.text = getString(R.string.edit_exercise)
                binding.btnNavigateBack.apply {
                    text = getString(R.string.back)
                    val backArrowIcon = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_arrow_back_ios_new_24
                    )
                    setCompoundDrawablesWithIntrinsicBounds(backArrowIcon, null, null, null)
                }
            }
        }
    }

    private fun observeExerciseTemplateState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exerciseTemplateEditorViewModel.exerciseTemplateState.collect { state ->
                    state.exerciseTemplate?.let { exerciseTemplate ->
                        binding.etNameExerciseTemplate.setText(exerciseTemplate.name)
                        binding.tvMuscleType.text = exerciseTemplate.exerciseMuscle.displayName
                        binding.tvResistanceType.text =
                            exerciseTemplate.exerciseResistance.displayName
                    }
                }
            }
        }
    }
}