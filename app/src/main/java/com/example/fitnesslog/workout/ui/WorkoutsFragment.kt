package com.example.fitnesslog.workout.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.fitnesslog.databinding.FragmentWorkoutsBinding
import com.example.fitnesslog.shared.ui.SharedViewModel
import kotlinx.coroutines.launch


class WorkoutsFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels { SharedViewModel.Factory }
    private var _binding: FragmentWorkoutsBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "WorkoutHomeFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.stateFlow.collect { sharedState ->
                    binding.tvWorkoutFragmentText.text =
                        sharedState.selectedProgram?.name ?: sharedState.toString()

                }
            }
        }
    }

}