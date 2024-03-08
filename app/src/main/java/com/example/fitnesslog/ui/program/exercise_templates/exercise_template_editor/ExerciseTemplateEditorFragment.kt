package com.example.fitnesslog.ui.program.exercise_templates.exercise_template_editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.EditorMode
import com.example.fitnesslog.core.enums.ExerciseMuscle
import com.example.fitnesslog.core.enums.ExerciseResistance
import com.example.fitnesslog.core.utils.constants.EXERCISE_MUSCLE
import com.example.fitnesslog.core.utils.constants.EXERCISE_RESISTANCE
import com.example.fitnesslog.core.utils.constants.EXERCISE_TEMPLATE_ID
import com.example.fitnesslog.core.utils.extensions.setThrottledOnClickListener
import com.example.fitnesslog.core.utils.extensions.textChangeFlow
import com.example.fitnesslog.core.utils.ui.showDiscardDialog
import com.example.fitnesslog.databinding.FragmentExerciseTemplateEditorBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackButtonListener()
    }

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
                if (savedInstanceState == null) {
                    initializeCreateMode()
                }
                binding.btnNavigateBack.setOnClickListener {
                    showDiscardDialog(requireContext()) {
                        exerciseTemplateEditorViewModel.onEvent(ExerciseTemplateEditorEvent.CancelCreate)
                        findNavController().popBackStack()
                    }
                }

                binding.btnSaveExerciseTemplate.setOnClickListener {
                    val navController = findNavController()
                    // add newly created template to the selected list
                    val exerciseTemplateId =
                        exerciseTemplateEditorViewModel.exerciseTemplateState.value.exerciseTemplate?.id
                            ?: return@setOnClickListener
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        EXERCISE_TEMPLATE_ID, exerciseTemplateId

                    )
                    navController.popBackStack()
                }
            }

            EditorMode.EDIT -> {
                if (savedInstanceState == null) {
                    exerciseTemplateEditorViewModel.onEvent(
                        ExerciseTemplateEditorEvent.RetrieveExistingExerciseTemplate(
                            args.exerciseTemplateId
                        )
                    )
                }
                binding.btnNavigateBack.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }

        setupUI()
        setupNavBackStackEntryObservers()
        observeExerciseTemplateState()
        setupTextChangeListener()

        binding.infoMuscle.setThrottledOnClickListener {
            val action =
                ExerciseTemplateEditorFragmentDirections.actionExerciseTemplateEditorFragmentToExerciseMuscleSelectDialog(
                    exerciseMuscle = exerciseTemplateEditorViewModel.exerciseTemplateState.value.exerciseTemplate?.exerciseMuscle!!
                )
            findNavController().navigate(action)
        }
        binding.infoResistance.setThrottledOnClickListener {
            val action =
                ExerciseTemplateEditorFragmentDirections.actionExerciseTemplateEditorFragmentToExerciseResistanceSelectDialog(
                    exerciseResistance = exerciseTemplateEditorViewModel.exerciseTemplateState.value.exerciseTemplate?.exerciseResistance!!
                )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeCreateMode() {
        exerciseTemplateEditorViewModel.onEvent(ExerciseTemplateEditorEvent.InitializeExerciseTemplate)
    }

    private fun setupTextChangeListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.etNameExerciseTemplate.textChangeFlow()
                    .debounce(500)
                    .collectLatest { name ->
                        exerciseTemplateEditorViewModel.onEvent(
                            ExerciseTemplateEditorEvent.UpdateName(
                                name
                            )
                        )
                    }
            }
        }
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
                binding.btnSaveExerciseTemplate.visibility = View.GONE
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

    private fun setupNavBackStackEntryObservers() {
        val navBackStackEntry =
            findNavController().getBackStackEntry(R.id.exerciseTemplateEditorFragment)

        val exerciseMuscleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(EXERCISE_MUSCLE)
            ) {
                val exerciseMuscle =
                    navBackStackEntry.savedStateHandle.get<ExerciseMuscle>(EXERCISE_MUSCLE)
                if (exerciseMuscle != null) {
                    exerciseTemplateEditorViewModel.onEvent(
                        ExerciseTemplateEditorEvent.UpdateExerciseMuscle(
                            exerciseMuscle
                        )
                    )
                }
            }
        }
        val exerciseResistanceObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(EXERCISE_RESISTANCE)
            ) {
                val exerciseResistance =
                    navBackStackEntry.savedStateHandle.get<ExerciseResistance>(EXERCISE_RESISTANCE)
                if (exerciseResistance != null) {
                    exerciseTemplateEditorViewModel.onEvent(
                        ExerciseTemplateEditorEvent.UpdateExerciseResistance(
                            exerciseResistance
                        )
                    )
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(exerciseMuscleObserver)
        navBackStackEntry.lifecycle.addObserver(exerciseResistanceObserver)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(exerciseMuscleObserver)
                navBackStackEntry.lifecycle.removeObserver(exerciseResistanceObserver)
            }
        })
    }

    private fun observeExerciseTemplateState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exerciseTemplateEditorViewModel.exerciseTemplateState.collect { state ->
                    state.exerciseTemplate?.let { exerciseTemplate ->
                        if (binding.etNameExerciseTemplate.text.toString() != exerciseTemplate.name) {
                            binding.etNameExerciseTemplate.setText(exerciseTemplate.name)
                            binding.etNameExerciseTemplate.setSelection(binding.etNameExerciseTemplate.length())
                        }
                        binding.tvMuscleType.text = exerciseTemplate.exerciseMuscle.displayName
                        binding.tvResistanceType.text =
                            exerciseTemplate.exerciseResistance.displayName
                    }
                }
            }
        }
    }

    private fun setBackButtonListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showDiscardDialog(
                requireContext()
            ) {
                exerciseTemplateEditorViewModel.onEvent(
                    ExerciseTemplateEditorEvent.CancelCreate
                )
                findNavController().popBackStack()
            }
        }
    }
}