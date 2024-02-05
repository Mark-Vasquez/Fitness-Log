package com.example.fitnesslog.program.ui.workout_templates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.program.domain.model.WorkoutTemplateExerciseWithName

class WorkoutTemplateExercisesAdapter(
    private val workoutTemplateExerciseClickListener: WorkoutTemplateExerciseClickListener
) :
    ListAdapter<WorkoutTemplateExerciseWithName, WorkoutTemplateExercisesAdapter.ViewHolder>(
        WorkoutTemplateExercisesDiffUtil
    ) {
    interface WorkoutTemplateExerciseClickListener {
        fun onExerciseClicked(workoutTemplateExerciseWithName: WorkoutTemplateExerciseWithName)
    }

    object WorkoutTemplateExercisesDiffUtil :
        DiffUtil.ItemCallback<WorkoutTemplateExerciseWithName>() {
        override fun areItemsTheSame(
            oldItem: WorkoutTemplateExerciseWithName,
            newItem: WorkoutTemplateExerciseWithName
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WorkoutTemplateExerciseWithName,
            newItem: WorkoutTemplateExerciseWithName
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