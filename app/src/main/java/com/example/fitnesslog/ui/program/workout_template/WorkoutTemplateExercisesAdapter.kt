package com.example.fitnesslog.ui.program.workout_template

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.data.entity.WorkoutTemplateExercise

class WorkoutTemplateExercisesAdapter(
    private val workoutTemplateExerciseClickListener: WorkoutTemplateExerciseClickListener
) :
    ListAdapter<WorkoutTemplateExercise, WorkoutTemplateExercisesAdapter.ViewHolder>(
        WorkoutTemplateExercisesDiffUtil
    ) {
    interface WorkoutTemplateExerciseClickListener {
        fun onExerciseClicked(workoutTemplateExercise: WorkoutTemplateExercise)
    }

    object WorkoutTemplateExercisesDiffUtil :
        DiffUtil.ItemCallback<WorkoutTemplateExercise>() {
        override fun areItemsTheSame(
            oldItem: WorkoutTemplateExercise,
            newItem: WorkoutTemplateExercise
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WorkoutTemplateExercise,
            newItem: WorkoutTemplateExercise
        ): Boolean {
            return oldItem.name == newItem.name
        }

    }

    class ViewHolder(itemView: View, clickAtPosition: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val tvNameItemWorkoutTemplateExercise: TextView =
            itemView.findViewById(R.id.tvNameItemWorkoutTemplateExercise)

        init {
            itemView.setOnClickListener {
                clickAtPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_workout_template_exercise, parent, false)
        return ViewHolder(itemView) { position ->
            workoutTemplateExerciseClickListener.onExerciseClicked(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workoutTemplateExercise = getItem(position)
        holder.apply {
            tvNameItemWorkoutTemplateExercise.text = workoutTemplateExercise.name
        }
    }

}