package com.example.fitnesslog.ui.program.workout_template

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
import com.example.fitnesslog.core.utils.extensions.textChangeFlow
import com.example.fitnesslog.core.utils.ui.showDeleteDialog
import com.example.fitnesslog.data.entity.WorkoutTemplateExercise
import com.example.fitnesslog.databinding.FragmentWorkoutTemplateBinding
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.util.Collections

class WorkoutTemplateFragment : Fragment() {
    private val workoutTemplateViewModel: WorkoutTemplateViewModel by viewModels {
        WorkoutTemplateViewModel.Factory(
            args.workoutTemplateId,
            appModule.workoutTemplateUseCases,
            appModule.exerciseTemplateUseCases
        )
    }

    private var _binding: FragmentWorkoutTemplateBinding? = null
    private val binding: FragmentWorkoutTemplateBinding get() = _binding!!
    private val args: WorkoutTemplateFragmentArgs by navArgs()
    private lateinit var workoutTemplateExercisesAdapter: WorkoutTemplateExercisesAdapter

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
        setupRecyclerView()
        observeWorkoutTemplateState()
        observeWorkoutTemplateExerciseState()
        setupWorkoutTemplateNameChangeListener()

        binding.btnNavigateBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnDeleteWorkout.setOnClickListener {
            val workoutTemplateName =
                workoutTemplateViewModel.workoutTemplateState.value.workoutTemplate?.name
            val message = if (workoutTemplateName.isNullOrEmpty()) {
                "Are you sure you want to delete this workout?"
            } else {
                "Are you sure you wan to delete \"${workoutTemplateName}\" workout?"
            }
            showDeleteDialog(requireContext(), "Delete Workout", message) {
                workoutTemplateViewModel.onEvent(WorkoutTemplateEvent.Delete)
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun observeWorkoutTemplateExerciseState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                workoutTemplateViewModel.workoutTemplateExercisesState.collectLatest {
                    workoutTemplateExercisesAdapter.submitList(it.workoutTemplateExercises)
                }
            }
        }
    }

    private fun setupWorkoutTemplateNameChangeListener() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.etNameWorkoutTemplate.textChangeFlow()
                    .debounce(500)
                    .collectLatest { name ->
                        workoutTemplateViewModel.onEvent(WorkoutTemplateEvent.UpdateName(name))
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

    private fun setupRecyclerView() {
        val rvWorkoutTemplateExercise = binding.rvWorkoutTemplateExercises
        workoutTemplateExercisesAdapter = WorkoutTemplateExercisesAdapter(object :
            WorkoutTemplateExercisesAdapter.WorkoutTemplateExerciseClickListener {
            override fun onExerciseClicked(workoutTemplateExercise: WorkoutTemplateExercise) {
                return
            }
        })
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        divider.apply {
            isLastItemDecorated = false
        }
        rvWorkoutTemplateExercise.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutTemplateExercisesAdapter
            addItemDecoration(divider)
        }

        class ItemTouchHelperCallback(private val adapter: WorkoutTemplateExercisesAdapter) :
            ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = 0
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val oldIndex = viewHolder.adapterPosition
                val newIndex = target.adapterPosition
                val updatedList = adapter.currentList.toMutableList()
                Collections.swap(updatedList, oldIndex, newIndex)
                adapter.submitList(updatedList)
                return true
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                viewHolder.itemView.alpha = 1.0f

                val updatedList = adapter.currentList
                workoutTemplateViewModel.onEvent(
                    WorkoutTemplateEvent.UpdateWorkoutTemplateExercisesOrder(
                        updatedList
                    )
                )
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        }

        val itemTouchHelper =
            ItemTouchHelper(ItemTouchHelperCallback(adapter = workoutTemplateExercisesAdapter))
        itemTouchHelper.attachToRecyclerView(rvWorkoutTemplateExercise)
    }
}