package com.example.fitnesslog.ui.program.workout_template.workout_template_exercise

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.data.entity.WorkoutTemplateExerciseSet

class WorkoutTemplateExerciseSetsAdapter(private val workoutTemplateExerciseSetClickListener: WorkoutTemplateExerciseSetClickListener) :
    ListAdapter<WorkoutTemplateExerciseSet, WorkoutTemplateExerciseSetsAdapter.ViewHolder>(
        WorkoutTemplateExerciseSetsDiffUtil
    ) {

    interface WorkoutTemplateExerciseSetClickListener {
        fun onSetNumberClicked(workoutTemplateExerciseSet: WorkoutTemplateExerciseSet)
        fun onRepNumberChanged(adapterPosition: Int, newGoalRep: Int)
        fun onWeightNumberChanged(adapterPosition: Int, newWeight: Int)
    }

    object WorkoutTemplateExerciseSetsDiffUtil :
        DiffUtil.ItemCallback<WorkoutTemplateExerciseSet>() {
        override fun areItemsTheSame(
            oldItem: WorkoutTemplateExerciseSet,
            newItem: WorkoutTemplateExerciseSet
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: WorkoutTemplateExerciseSet,
            newItem: WorkoutTemplateExerciseSet
        ): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(
        itemView: View,
        clickAtPosition: (position: Int, view: View) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {
        val tvSetNumber: TextView = itemView.findViewById(R.id.tvSetNumber)
        val etRepNumber: EditText = itemView.findViewById(R.id.etRepNumber)
        val etWeightNumber: EditText = itemView.findViewById(R.id.etWeightNumber)

        private val repNumberTextWatcher = RepNumberTextWatcher { newGoalRep ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                workoutTemplateExerciseSetClickListener.onRepNumberChanged(
                    adapterPosition,
                    newGoalRep
                )
            }
        }

        private val weightNumberTextWatcher = WeightNumberTextWatcher { newWeight ->
            if (adapterPosition != RecyclerView.NO_POSITION) {
                workoutTemplateExerciseSetClickListener.onWeightNumberChanged(
                    adapterPosition,
                    newWeight
                )
            }
        }

        init {
            tvSetNumber.setOnClickListener {
                clickAtPosition(adapterPosition, it)
            }
        }

        fun enableTextWatcher() {
            etRepNumber.addTextChangedListener(repNumberTextWatcher)
            etWeightNumber.addTextChangedListener(weightNumberTextWatcher)
        }

        fun disableTextWatcher() {
            etRepNumber.removeTextChangedListener(repNumberTextWatcher)
            etWeightNumber.removeTextChangedListener(weightNumberTextWatcher)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout_template_exercise_set, parent, false)
        return ViewHolder(itemView) { position, view ->
            val workoutTemplateExerciseSet = getItem(position)
            if (view.id == R.id.tvSetNumber) {
                workoutTemplateExerciseSetClickListener.onSetNumberClicked(
                    workoutTemplateExerciseSet
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val workoutTemplateExerciseSet = getItem(position)
        holder.apply {
            tvSetNumber.text = itemView.context.getString(
                R.string.set_number,
                workoutTemplateExerciseSet.position + 1
            )
            val newRepText = workoutTemplateExerciseSet.goalReps.toString()
            if (etRepNumber.text.toString() != newRepText) {
                etRepNumber.setText(newRepText)
                etRepNumber.setSelection(etRepNumber.length())
            }
            val newWeightText = workoutTemplateExerciseSet.weightInLbs.toString()
            if (etWeightNumber.text.toString() != newWeightText) {
                etWeightNumber.setText(newWeightText)
                etWeightNumber.setSelection((etWeightNumber.length()))
            }
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.enableTextWatcher()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.disableTextWatcher()
    }

    inner class RepNumberTextWatcher(private val onRepNumberChanged: (Int) -> Unit) :
        TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val newRepNumber = when {
                s.isNullOrEmpty() || s.toString() == "0" -> 1
                else -> s.toString().toIntOrNull() ?: 1
            }
            onRepNumberChanged(newRepNumber)
        }
    }

    inner class WeightNumberTextWatcher(private val onWeightNumberChanged: (Int) -> Unit) :
        TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            val newWeightNumber = s?.toString()?.toIntOrNull() ?: 0
            onWeightNumberChanged(newWeightNumber)
        }
    }
}