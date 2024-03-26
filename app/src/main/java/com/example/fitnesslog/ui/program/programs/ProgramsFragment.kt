package com.example.fitnesslog.ui.program.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.core.utils.ui.GridSpacingItemDecoration
import com.example.fitnesslog.databinding.FragmentProgramsBinding
import com.example.fitnesslog.domain.model.ProgramWithWorkoutCount
import com.example.fitnesslog.ui.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class ProgramsFragment : Fragment() {

    // Scoped to this specific fragment instance
    private val programsViewModel: ProgramsViewModel by viewModels { ProgramsViewModel.Factory }

    // Scoped to parent activity
    private val sharedViewModel: SharedViewModel by activityViewModels { SharedViewModel.Factory }

    // ViewBinding reference to the ViewObject instance from onCreateView to access FragmentProgram's layout views
    private var _binding: FragmentProgramsBinding? = null

    // Used to safely access an immutable version of binding, guaranteed that _binding will have a value when accessed
    private val binding get() = _binding!!
    private lateinit var programsAdapter: ProgramsAdapter
    private lateinit var rvPrograms: RecyclerView

    companion object {
        const val TAG = "ProgramsFragment"
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgramsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeProgramsState()

        binding.fabCreateProgram.setOnClickListener {
            val action =
                ProgramsFragmentDirections.actionProgramsFragmentToProgramCreateFragment()
            findNavController().navigate(action)
        }

        binding.fabEditProgram.setOnClickListener {
            val selectedProgramId = sharedViewModel.stateFlow.value.selectedProgram?.id
            if (selectedProgramId == null) {
                Snackbar.make(
                    view,
                    "Error selecting `null` selectedProgramId",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val action =
                ProgramsFragmentDirections.actionProgramsFragmentToProgramEditFragment(

                    programId = selectedProgramId
                )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Releases the ViewBinding instance's references to the ViewObject instance that was destroyed
        // Garbage collection reclaims memory from both instances
        _binding = null
    }

    private fun setupRecyclerView() {
        rvPrograms = binding.rvPrograms
        rvPrograms.layoutManager = GridLayoutManager(requireContext(), 2)
        rvPrograms.addItemDecoration(GridSpacingItemDecoration(25))

        // Pass in an instance of the listener object and implement what should happen when onProgramClicked is called
        programsAdapter =
            ProgramsAdapter(object : ProgramsAdapter.ProgramClickListener {
                override fun onProgramClicked(program: ProgramWithWorkoutCount) {
                    programsViewModel.onEvent(ProgramsEvent.Select(program))
                }
            })
        rvPrograms.adapter = programsAdapter
    }


    /**
     * Sets up a lifecycle aware coroutine block that collects state changes from programsViewModel
     * The collection is active as long as the fragment's view is in a 'STARTED' state or beyond.
     * The collection block is executed when the fragment starts and is automatically
     * paused and resumed based on the fragment's lifecycle
     * Collect listens for any changes to the _stateflow
     */
    private fun observeProgramsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programsViewModel.programsState.collect { programState ->
                    programsAdapter.submitList(programState.programs) {
                        rvPrograms.scrollToPosition(0)
                    }
                }
            }
        }
    }

}