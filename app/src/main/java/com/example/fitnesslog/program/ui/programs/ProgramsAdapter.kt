package com.example.fitnesslog.program.ui.programs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount

/**
 * ListAdapter wraps around the traditional RecyclerView.Adapter to efficiently make updates to the rendered list
 * It combines DiffUtil and AsyncListDiffer. DiffUtil under the hood calculates the diffs for minimal
 * UI updates. AsyncListDiffer sends these calculations to a background thread, which makes it more performant
 * with smoother UI animations.
 * This approach also avoids having to manually keep track of every item's movement with notifyItemMoved()
 * or worse, notifyDataSetChanged() which rebinds and re-renders the whole list for any small changes
 */
class ProgramsAdapter(
    private val programClickListener: ProgramClickListener
) :
    ListAdapter<ProgramWithWorkoutCount, ProgramsAdapter.ViewHolder>(ProgramsDiffUtil) {

    object ProgramsDiffUtil : DiffUtil.ItemCallback<ProgramWithWorkoutCount>() {
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

    // Allows the onProgramClicked to have custom implementation when passing into ProgramsAdapter instance
    interface ProgramClickListener {
        fun onProgramClicked(program: ProgramWithWorkoutCount)
    }

    class ViewHolder(itemView: View, clickAtPosition: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        val tvProgramName: TextView = itemView.findViewById(R.id.tvProgramName)
        val tvNumberOfWorkouts: TextView = itemView.findViewById(R.id.tvNumberOfWorkouts)

        // Sets a click listener one time when instantiating a ViewHolder,
        // Rather than on an itemView in onBindViewHolder each time it binds a new item
        // Pass in a lambda to the listener that will call the clickAtPosition lambda with
        // the dynamic `adapterPosition` property
        init {
            itemView.setOnClickListener {
                clickAtPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_program, parent, false)
        // Pass in a lambda that takes a position and will call onProgramClicked with the program
        return ViewHolder(itemView) { position ->
            programClickListener.onProgramClicked(getItem(position))
        }
    }


    // Do not setClickListener to the itemView here because costly and gets called everytime new item in view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val program = getItem(position)
        val context = holder.itemView.context
        val workoutString = if (program.workoutCount == 1) "workout" else "workouts"
        holder.apply {
            tvProgramName.text = program.name
            tvNumberOfWorkouts.text = itemView.context.getString(
                R.string.workout_count,
                program.workoutCount,
                workoutString
            )

            // Render a highlighted background if selected
            if (position == 0 && program.isSelected) {
                val selectedBackground =
                    ContextCompat.getDrawable(context, R.drawable.bg_program_selected)
                itemView.background = selectedBackground
            } else {
                val defaultBackground =
                    ContextCompat.getDrawable(context, R.drawable.bg_program_unselected)
                itemView.background = defaultBackground
            }
        }
    }
}