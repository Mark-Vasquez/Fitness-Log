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
import com.example.fitnesslog.core.enums.ExerciseTemplateOperation
import com.example.fitnesslog.core.utils.constants.EXERCISE_TEMPLATE_ID
import com.example.fitnesslog.core.utils.extensions.setThrottledOnClickListener
import com.example.fitnesslog.core.utils.ui.showDeleteDialog
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
        observeSelectedExercisesToIsDefaultState()
        retrievePreviousDestinationResult()

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

        binding.fabDeleteExercises.setOnClickListener {
            val selectedCustomExercises =
                exerciseTemplatesViewModel.selectedExercisesToIsDefaultState.value.filter { !it.value }.keys.toList()
            if (selectedCustomExercises.isNotEmpty()) {
                val title = if (selectedCustomExercises.size > 1) {
                    "Delete Custom Exercises?"
                } else {
                    "Delete Custom Exercise?"
                }
                val message =
                    "Deleting these exercises will remove them only from this list, not from any of your individual workouts."
                showDeleteDialog(requireContext(), title, message) {
                    exerciseTemplatesViewModel.onEvent(
                        ExerciseTemplateEvent.DeleteSelectedExercises(
                            selectedCustomExercises
                        )
                    )
                }
            }

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

    private fun observeSelectedExercisesToIsDefaultState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exerciseTemplatesViewModel.selectedExercisesToIsDefaultState.collectLatest { selectedExercisesToIsDefaultMap ->
                    exerciseTemplatesAdapter.submitMap(selectedExercisesToIsDefaultMap)
                    if (selectedExercisesToIsDefaultMap.isNotEmpty()) {
                        binding.btnAdd.apply {
                            text = getString(
                                R.string.btn_add_count,
                                selectedExercisesToIsDefaultMap.size
                            )
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

                    val selectedCustomExerciseCount =
                        selectedExercisesToIsDefaultMap.count { !it.value }
                    if (selectedCustomExerciseCount > 0) {
                        binding.fabDeleteExercises.apply {
                            text = getString(R.string.btn_delete_count, selectedCustomExerciseCount)
                            isEnabled = true
                            visibility = View.VISIBLE
                            extend()
                        }
                    } else {
                        binding.fabDeleteExercises.apply {
                            isEnabled = false
                            visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun retrievePreviousDestinationResult() {
        // Retrieve new id from ExerciseTemplateEditor after creating or deleting an Exercise Template
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Triple<Int, ExerciseTemplateOperation, Boolean>>(
            EXERCISE_TEMPLATE_ID
        )?.observe(viewLifecycleOwner) { (exerciseTemplateId, operation, isDefault) ->
            when (operation) {
                ExerciseTemplateOperation.CREATE -> {
                    exerciseTemplatesViewModel.onEvent(
                        ExerciseTemplateEvent.AddToTemplateSelect(exerciseTemplateId, isDefault)
                    )
                    // Notify the view holder with the template id to update its ui to reflect checked state
                    val adapterPosition =
                        exerciseTemplatesAdapter.currentList.indexOfFirst { it.id == exerciseTemplateId }
                    if (adapterPosition != -1) {
                        exerciseTemplatesAdapter.notifyItemChanged(adapterPosition)
                    }
                }

                ExerciseTemplateOperation.DELETE -> {
                    exerciseTemplatesViewModel.onEvent(
                        ExerciseTemplateEvent.RemoveFromTemplateSelect(exerciseTemplateId)
                    )
                }
            }

            // Remove the savedStateHandle value after consuming it once, so that the observe callback
            // does not re-run again on screen rotation,
            findNavController().currentBackStackEntry?.savedStateHandle?.remove<Triple<Int, ExerciseTemplateOperation, Boolean>>(
                EXERCISE_TEMPLATE_ID
            )
        }
    }

    private fun setupRecyclerView() {
        val rvExerciseTemplates = binding.rvExerciseTemplates
        exerciseTemplatesAdapter = ExerciseTemplatesAdapter(object :
            ExerciseTemplatesAdapter.ExerciseTemplateClickListener {
            override fun onExerciseTemplateClicked(exerciseTemplateId: Int, isDefault: Boolean) {
                exerciseTemplatesViewModel.onEvent(
                    ExerciseTemplateEvent.ToggleTemplateSelect(
                        exerciseTemplateId, isDefault
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