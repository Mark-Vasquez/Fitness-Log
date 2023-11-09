package com.example.fitnesslog.program.ui.programs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

class ProgramsAdapter(
    private val programClickListener: ProgramClickListener
) :
    ListAdapter<ProgramWithWorkoutCount, ProgramsAdapter.ViewHolder>(ProgramsDiff) {

    object ProgramsDiff : DiffUtil.ItemCallback<ProgramWithWorkoutCount>() {
        override fun areItemsTheSame(
            oldItem: ProgramWithWorkoutCount,
            newItem: ProgramWithWorkoutCount
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProgramWithWorkoutCount,
            newItem: ProgramWithWorkoutCount
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface ProgramClickListener {
        fun onProgramClicked(program: ProgramWithWorkoutCount)
    }

    class ViewHolder(itemView: View, clickAtPosition: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val tvProgramName: TextView = itemView.findViewById(R.id.tvProgramName)
        val tvNumberOfWorkouts: TextView = itemView.findViewById(R.id.tvNumberOfWorkouts)

        init {
            itemView.setOnClickListener {
                clickAtPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_program, parent, false)
        return ViewHolder(itemView) { position ->
            programClickListener.onProgramClicked(getItem(position))
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val program = getItem(position)
        val workoutString = if (program.workoutCount == 1) "workout" else "workouts"
        holder.apply {
            tvProgramName.text = program.name
            tvNumberOfWorkouts.text = itemView.context.getString(
                R.string.workout_count,
                program.workoutCount,
                workoutString
            )
        }
    }
}