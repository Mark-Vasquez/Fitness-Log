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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.data.entity.WorkoutTemplateExerciseSet
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
        rvExerciseSets.itemAnimator = null
        workoutTemplateExerciseSetsAdapter = WorkoutTemplateExerciseSetsAdapter(object :
            WorkoutTemplateExerciseSetsAdapter.WorkoutTemplateExerciseSetClickListener {
            override fun onSetNumberClicked(workoutTemplateExerciseSet: WorkoutTemplateExerciseSet) {
//
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
        rvExerciseSets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutTemplateExerciseSetsAdapter
        }
    }
}