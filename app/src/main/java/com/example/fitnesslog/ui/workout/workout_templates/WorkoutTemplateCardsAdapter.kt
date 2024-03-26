package com.example.fitnesslog.ui.workout.workout_templates

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.data.entity.WorkoutTemplate

class WorkoutTemplateCardsAdapter(private val workoutTemplateCardClickListener: WorkoutTemplateCardClickListener) :
    ListAdapter<WorkoutTemplate, WorkoutTemplateCardsAdapter.ViewHolder>(
        WorkoutTemplateCardsDiffUtil
    ) {

    interface WorkoutTemplateCardClickListener {
        fun onWorkoutTemplateCardClicked(workoutTemplate: WorkoutTemplate)
    }

    object WorkoutTemplateCardsDiffUtil : DiffUtil.ItemCallback<WorkoutTemplate>() {
        override fun areItemsTheSame(oldItem: WorkoutTemplate, newItem: WorkoutTemplate): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WorkoutTemplate,
            newItem: WorkoutTemplate
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(itemView: View, clickAtPosition: (position: Int, view: View) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val tvNameItemWorkoutTemplate: TextView =
            itemView.findViewById(R.id.tvNameItemWorkoutTemplate)
        val tvWorkoutDate: TextView = itemView.findViewById(R.id.tvWorkoutDate)
        val rvWorkoutTemplateCardExercises: RecyclerView =
            itemView.findViewById(R.id.rvWorkoutTemplateCardExercises)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}