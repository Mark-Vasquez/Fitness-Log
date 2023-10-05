package com.example.fitnesslog.program.ui.program.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.example.fitnesslog.program.data.entity.Program

class ProgramAdapter(private val programs: List<Program>) :
    RecyclerView.Adapter<ProgramAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProgramName: TextView = view.findViewById(R.id.tvProgramName)
        val tvNumberOfWorkouts: TextView = view.findViewById(R.id.tvNumberOfWorkouts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.program_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return programs.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val program = programs[position]
        holder.apply {
//            tvProgramName.text = program.programName
//            tvNumberOfWorkouts.text = "${program.numberOfWorkouts} workouts"
        }
    }
}