package com.example.fitnesslog.program.ui.program

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.program.data.entity.WorkoutTemplate

class WorkoutTemplatesAdapter(
    private val workoutTemplateClickListener: WorkoutTemplateClickListener
) :
    ListAdapter<WorkoutTemplate, WorkoutTemplatesAdapter.ViewHolder>(WorkoutsDiffUtil) {
    interface WorkoutTemplateClickListener {
        fun onWorkoutTemplateClicked(workoutTemplate: WorkoutTemplate)
    }

    object WorkoutsDiffUtil : DiffUtil.ItemCallback<WorkoutTemplate>() {
        override fun areItemsTheSame(oldItem: WorkoutTemplate, newItem: WorkoutTemplate): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WorkoutTemplate,
            newItem: WorkoutTemplate
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }

    class ViewHolder(itemView: View, clickAtPosition: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val tvNameItemWorkout: TextView = itemView.findViewById(R.id.tvNameItemWorkout)

        init {
            itemView.setOnClickListener {
                clickAtPosition(adapterPosition)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_workout_template, parent, false)
        return ViewHolder(itemView) { position ->
            workoutTemplateClickListener.onWorkoutTemplateClicked(getItem(position))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workoutTemplate = getItem(position)
        holder.apply {
            tvNameItemWorkout.text = workoutTemplate.name
        }
    }


}