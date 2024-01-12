package com.example.fitnesslog.program.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.core.utils.GridSpacingItemDecoration
import com.example.fitnesslog.databinding.FragmentProgramsBinding
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount
import kotlinx.coroutines.launch


class ProgramsFragment : Fragment() {

    private val programsViewModel: ProgramsViewModel by activityViewModels { ProgramsViewModel.Factory }
    private lateinit var programsAdapter: ProgramsAdapter
    private lateinit var rvPrograms: RecyclerView

    // ViewBinding reference to the ViewObject instance from onCreateView to access FragmentProgram's layout views
    private var _binding: FragmentProgramsBinding? = null

    // Used to safely access an immutable version of binding, guaranteed that _binding will have a value when accessed
    private val binding get() = _binding!!

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
        observeViewModel()

        binding.fabCreateProgram.setOnClickListener {
            programsViewModel.onEvent(ProgramsEvent.ShowCreateForm)
            // TODO: Navigate to ProgramCreateFragment with ref. to initializedProgramId
            val action = ProgramsFragmentDirections.actionProgramFragmentToProgramCreateFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Releases the ViewBinding instance's references to the ViewObject instance destroyed
        // Garbage collection reclaims memory from both instances
        _binding = null
    }

    private fun setupRecyclerView() {
        rvPrograms = binding.rvPrograms
        rvPrograms.layoutManager = GridLayoutManager(context, 2)
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
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programsViewModel.stateFlow.collect { programState ->
                    updateRecyclerView(programState.programs)
                }
            }
        }
    }

    // When submitList() is used to populate or update the recyclerView list,
    // the ListAdapter calculates the diffs internally without having to manually update and notifyDataSetChanged()
    private fun updateRecyclerView(programs: List<ProgramWithWorkoutCount>) {
        programsAdapter.submitList(programs) {
            rvPrograms.scrollToPosition(0)
        }
    }


}