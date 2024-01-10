package com.example.fitnesslog.workout.ui.workout_home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.fitnesslog.R
import com.example.fitnesslog.shared.ui.SharedViewModel
import kotlinx.coroutines.launch


class WorkoutHomeFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels { SharedViewModel.Factory }

    companion object {
        const val TAG = "WorkoutHomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.stateFlow.collect { sharedState ->
                    Log.d(TAG, sharedState.selectedProgram?.name.toString())

                }
            }
        }
    }

}