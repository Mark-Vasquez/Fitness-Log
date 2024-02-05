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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.FitnessLogApp.Companion.workoutTemplateModule
import com.example.fitnesslog.databinding.FragmentWorkoutTemplateBinding
import com.example.fitnesslog.program.domain.model.WorkoutTemplateExerciseWithName
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Collections

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

        binding.btnNavigateBack.setOnClickListener {
            findNavController().popBackStack()
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
            override fun onExerciseClicked(workoutTemplateExerciseWithName: WorkoutTemplateExerciseWithName) {
                TODO("Not yet implemented")
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
                // TODO("send list through event to db to update")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        }

        val itemTouchHelper =
            ItemTouchHelper(ItemTouchHelperCallback(adapter = workoutTemplateExercisesAdapter))
        itemTouchHelper.attachToRecyclerView(rvWorkoutTemplateExercise)
    }
}