package com.example.fitnesslog.ui.program.workout_template.workout_template_exercise

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.core.utils.helpers.CustomItemTouchHelperCallback
import com.example.fitnesslog.data.entity.WorkoutTemplateExerciseSet
import com.example.fitnesslog.databinding.FragmentWorkoutTemplateExerciseBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.divider.MaterialDividerItemDecoration
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
    private lateinit var workoutTemplateExerciseSetsAdapter: WorkoutTemplateExerciseSetsAdapter

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
        setupRecyclerView()
        observeWorkoutTemplateExerciseState()
        observeWorkoutTemplateExerciseSetsState()

        binding.btnNavigateBack.setOnClickListener {
            findNavController().popBackStack()
        }
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

    private fun observeWorkoutTemplateExerciseSetsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                workoutTemplateExerciseViewModel.workoutTemplateExerciseSetsState.collectLatest {
                    workoutTemplateExerciseSetsAdapter.submitList(it.workoutTemplateExerciseSets)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        val rvExerciseSets = binding.rvExerciseSets
        workoutTemplateExerciseSetsAdapter = WorkoutTemplateExerciseSetsAdapter(object :
            WorkoutTemplateExerciseSetsAdapter.WorkoutTemplateExerciseSetClickListener {
            override fun onSetNumberClicked(workoutTemplateExerciseSet: WorkoutTemplateExerciseSet) {
                // Show menu to delete or copy
            }

            override fun onRepNumberChanged(adapterPosition: Int, newGoalRep: Int) {
                val workoutTemplateExerciseSet =
                    workoutTemplateExerciseSetsAdapter.currentList[adapterPosition]
                if (workoutTemplateExerciseSet != null) {
                    workoutTemplateExerciseViewModel.onEvent(
                        WorkoutTemplateExerciseEvent.UpdateSetGoalRep(
                            workoutTemplateExerciseSet,
                            newGoalRep
                        )
                    )
                }
            }

            override fun onWeightNumberChanged(adapterPosition: Int, newWeight: Int) {
                val workoutTemplateExerciseSet =
                    workoutTemplateExerciseSetsAdapter.currentList[adapterPosition]
                if (workoutTemplateExerciseSet != null) {
                    workoutTemplateExerciseViewModel.onEvent(
                        WorkoutTemplateExerciseEvent.UpdateSetWeight(
                            workoutTemplateExerciseSet,
                            newWeight
                        )
                    )
                }
            }
        })
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.apply {
            isLastItemDecorated = false
            dividerThickness = 4
            dividerInsetStart = 15
            dividerInsetEnd = 15
            dividerColor = MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorOnPrimaryContainer,
                Color.BLACK
            )
        }
        rvExerciseSets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutTemplateExerciseSetsAdapter
            itemAnimator = null
            addItemDecoration(divider)
        }


        val swipeCallback = object : CustomItemTouchHelperCallback(
            requireContext(),
            dragDirections = 0,
            swipeDirections = ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val workoutTemplateExerciseSet =
                    workoutTemplateExerciseSetsAdapter.currentList[position]
                val workoutTemplateExerciseSetId = workoutTemplateExerciseSet.id ?: return
                workoutTemplateExerciseViewModel.onEvent(
                    WorkoutTemplateExerciseEvent.DeleteSet(
                        workoutTemplateExerciseSetId,
                        workoutTemplateExerciseSet.workoutTemplateExerciseId
                    )
                )
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
            }
        }

        val itemTouchHelper =
            ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(rvExerciseSets)
    }
}