package com.example.fitnesslog.ui.program.exercise_templates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.EditorMode
import com.example.fitnesslog.core.utils.constants.EXERCISE_TEMPLATE_ID
import com.example.fitnesslog.core.utils.extensions.setThrottledOnClickListener
import com.example.fitnesslog.core.utils.ui.showDiscardDialog
import com.example.fitnesslog.data.entity.ExerciseTemplate
import com.example.fitnesslog.databinding.FragmentExerciseTemplatesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExerciseTemplatesFragment : Fragment() {
    private val exerciseTemplatesViewModel: ExerciseTemplatesViewModel by viewModels {
        ExerciseTemplatesViewModel.Factory(
            args.workoutTemplateId,
            appModule.exerciseTemplateUseCases,
            appModule.workoutTemplateUseCases
        )
    }
    private var _binding: FragmentExerciseTemplatesBinding? = null
    private val binding: FragmentExerciseTemplatesBinding get() = _binding!!
    private val args: ExerciseTemplatesFragmentArgs by navArgs()
    private lateinit var exerciseTemplatesAdapter: ExerciseTemplatesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackButtonListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExerciseTemplatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeExerciseTemplatesState()
        observeCheckedExerciseTemplatesState()

        binding.btnCancel.setThrottledOnClickListener {
            showDiscardDialog(requireContext()) {
                findNavController().popBackStack()
            }
        }

        binding.btnAdd.setThrottledOnClickListener {
            exerciseTemplatesViewModel.onEvent(ExerciseTemplateEvent.SubmitSelectedExercises)
            findNavController().popBackStack()
        }

        binding.fabCreateExercise.setOnClickListener {
            val action =
                ExerciseTemplatesFragmentDirections.actionExerciseTemplatesFragmentToExerciseTemplateEditorFragment(
                    editorMode = EditorMode.CREATE
                )
            findNavController().navigate(action)
        }

        // Retrieve new id from ExerciseTemplateEditor after creating a new Exercise Template
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Int>(
            EXERCISE_TEMPLATE_ID
        )?.observe(viewLifecycleOwner) { exerciseTemplateId ->
            exerciseTemplatesViewModel.onEvent(
                ExerciseTemplateEvent.ToggleCheckbox(
                    exerciseTemplateId
                )
            )

            // Remove the savedStateHandle value after consuming it once, so that the observe callback
            // does not re-run again on screen rotation, un-toggling the new template
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Int>(
                EXERCISE_TEMPLATE_ID
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeExerciseTemplatesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exerciseTemplatesViewModel.exerciseTemplatesState.collectLatest {
                    exerciseTemplatesAdapter.submitList(it.exerciseTemplates)
                }
            }
        }
    }

    private fun observeCheckedExerciseTemplatesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exerciseTemplatesViewModel.checkedExerciseTemplatesState.collectLatest {
                    exerciseTemplatesAdapter.submitSet(it)
                    if (it.isNotEmpty()) {
                        binding.btnAdd.apply {
                            text = getString(R.string.btn_add_count, it.size)
                            alpha = 1F
                            isEnabled = true
                        }
                    } else {
                        binding.btnAdd.apply {
                            text = getString(R.string.add)
                            alpha = 0.3F
                            isEnabled = false
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val rvExerciseTemplates = binding.rvExerciseTemplates
        exerciseTemplatesAdapter = ExerciseTemplatesAdapter(object :
            ExerciseTemplatesAdapter.ExerciseTemplateClickListener {
            override fun onExerciseTemplateClicked(exerciseTemplateId: Int) {
                exerciseTemplatesViewModel.onEvent(
                    ExerciseTemplateEvent.ToggleCheckbox(
                        exerciseTemplateId
                    )
                )
            }

            override fun onIconClicked(exerciseTemplate: ExerciseTemplate) {
                if (exerciseTemplate.id == null) return
                val action = if (exerciseTemplate.isDefault) {
                    ExerciseTemplatesFragmentDirections.actionExerciseTemplatesFragmentToDefaultExerciseInfoFragment(
                        exerciseTemplateId = exerciseTemplate.id
                    )
                } else {
                    ExerciseTemplatesFragmentDirections.actionExerciseTemplatesFragmentToExerciseTemplateEditorFragment(
                        exerciseTemplateId = exerciseTemplate.id,
                        editorMode = EditorMode.EDIT
                    )
                }
                findNavController().navigate(action)
            }

        })
        rvExerciseTemplates.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = exerciseTemplatesAdapter
        }
    }

    private fun setBackButtonListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showDiscardDialog(requireContext()) {
                findNavController().popBackStack()
            }
        }
    }
}