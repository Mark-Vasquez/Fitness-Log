package com.example.fitnesslog.ui.program.exercise_templates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.data.entity.ExerciseTemplate

class ExerciseTemplatesAdapter(private val exerciseTemplateClickListener: ExerciseTemplateClickListener) :
    ListAdapter<ExerciseTemplate, ExerciseTemplatesAdapter.ViewHolder>(
        ExerciseTemplatesDiffUtil
    ) {

    private lateinit var selectedExerciseToIsDefaultMap: Map<Int, Boolean>

    fun submitMap(newMap: Map<Int, Boolean>) {
        selectedExerciseToIsDefaultMap = newMap
    }

    interface ExerciseTemplateClickListener {
        fun onExerciseTemplateClicked(exerciseTemplateId: Int, isDefault: Boolean)
        fun onIconClicked(exerciseTemplate: ExerciseTemplate)
    }

    object ExerciseTemplatesDiffUtil : DiffUtil.ItemCallback<ExerciseTemplate>() {
        override fun areItemsTheSame(
            oldItem: ExerciseTemplate,
            newItem: ExerciseTemplate
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ExerciseTemplate,
            newItem: ExerciseTemplate
        ): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(
        itemView: View,
        clickAtPosition: (position: Int, view: View) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {
        val cbExercise: CheckBox = itemView.findViewById(R.id.cbExercise)
        val tvNameItemExerciseTemplate: TextView =
            itemView.findViewById(R.id.tvNameItemExerciseTemplate)
        val ivInfoEditIcon: ImageView = itemView.findViewById(R.id.ivInfoEditIcon)

        init {
            ivInfoEditIcon.setOnClickListener {
                clickAtPosition(adapterPosition, it)
            }
            itemView.setOnClickListener {
                clickAtPosition(adapterPosition, it)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_template, parent, false)
        return ViewHolder(itemView) { position, view ->
            val exerciseTemplate = getItem(position)
            exerciseTemplate.id?.let { exerciseTemplateId ->
                if (view.id == R.id.ivInfoEditIcon) {
                    // If only icon view clicked
                    exerciseTemplateClickListener.onIconClicked(exerciseTemplate)
                } else {
                    // If whole item clicked
                    exerciseTemplateClickListener.onExerciseTemplateClicked(
                        exerciseTemplateId,
                        exerciseTemplate.isDefault
                    )
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exerciseTemplate = getItem(position)
        holder.apply {
            cbExercise.isChecked = exerciseTemplate.id in selectedExerciseToIsDefaultMap
            tvNameItemExerciseTemplate.text = exerciseTemplate.name
            val exerciseTemplateIcon =
                if (exerciseTemplate.isDefault) R.drawable.info_24px else R.drawable.baseline_edit_note_24
            ivInfoEditIcon.setImageResource(exerciseTemplateIcon)
        }
    }
}