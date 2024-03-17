package com.example.fitnesslog.ui.program.workout_template.workout_template_exercise

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.FitnessLogApp.Companion.appModule
import com.example.fitnesslog.R
import com.example.fitnesslog.data.entity.WorkoutTemplateExerciseSet
import com.example.fitnesslog.databinding.FragmentWorkoutTemplateExerciseBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

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
        }
        rvExerciseSets.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workoutTemplateExerciseSetsAdapter
            itemAnimator = null
            addItemDecoration(divider)
        }

        class ItemTouchHelperCallback(
            private val adapter: WorkoutTemplateExerciseSetsAdapter,
            private val context: Context
        ) :
            ItemTouchHelper.Callback() {

            private val iconDrawable =
                ContextCompat.getDrawable(context, R.drawable.delete_24px)?.mutate()
            private val iconColor = MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.colorOnErrorContainer,
                Color.WHITE
            )

            private val backgroundColor = MaterialColors.getColor(
                context,
                com.google.android.material.R.attr.colorErrorContainer,
                Color.parseColor("#f44336")
            )
            private val backgroundDrawable = ColorDrawable(backgroundColor)

            init {
                // Ensure the drawable is not null before applying the tint
                iconDrawable?.let {
                    DrawableCompat.wrap(it)
                    DrawableCompat.setTint(it, iconColor)
                }
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = 0
                val swipeFlags = ItemTouchHelper.LEFT
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                val isCancelled = dX == 0f && !isCurrentlyActive

                if (isCancelled || iconDrawable == null) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                    return
                }

                // Sets the red delete background position to show only how far dX swiped left from right
                backgroundDrawable.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                backgroundDrawable.draw(c)

                // Get the position values for the delete icon bounds
                val iconMargin = (itemHeight - iconDrawable.intrinsicHeight) / 2
                val iconTop = itemView.top + iconMargin
                val iconLeft = itemView.right - iconMargin - iconDrawable.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                val iconBottom = itemView.bottom - iconMargin

                // Only show icon if background is big enough to fit the icon
                if (abs(dX) > abs(iconRight - iconLeft) + (iconMargin)) {
                    iconDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    iconDrawable.draw(c)
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }

        val itemTouchHelper =
            ItemTouchHelper(
                ItemTouchHelperCallback(
                    adapter = workoutTemplateExerciseSetsAdapter,
                    context = requireContext()
                )
            )
        itemTouchHelper.attachToRecyclerView(rvExerciseSets)
    }
}