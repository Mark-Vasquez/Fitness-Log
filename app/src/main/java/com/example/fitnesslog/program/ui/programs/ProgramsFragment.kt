package com.example.fitnesslog.program.ui.programs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.core.ui.viewModelFactoryHelper
import com.example.fitnesslog.core.utils.GridSpacingItemDecoration
import com.example.fitnesslog.databinding.FragmentProgramBinding
import com.example.fitnesslog.program.domain.model.ProgramWithWorkoutCount
import com.example.fitnesslog.shared.ui.SharedEvent
import com.example.fitnesslog.shared.ui.SharedViewModel
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProgramsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProgramsFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var programsViewModel: ProgramsViewModel
    private lateinit var programsAdapter: ProgramsAdapter
    private lateinit var rvPrograms: RecyclerView
    private var _binding: FragmentProgramBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "ProgramsFragment"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject dependencies into the ViewModel instance using the globally available module dependencies
        val programsViewModelFactory =
            viewModelFactoryHelper {
                ProgramsViewModel(
                    programModule.programUseCases, sharedViewModel
                )
            }
        programsViewModel =
            ViewModelProvider(this, programsViewModelFactory)[ProgramsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgramBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()

        binding.fabCreateProgram.setOnClickListener {
            programsViewModel.onEvent(ProgramsEvent.ShowCreateForm)
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
                    sharedViewModel.onEvent(SharedEvent.SelectProgram(program))
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
                    populateRecyclerView(programState.programs)
                    handleModalEvent(programState.modalEvent)
                    Log.d(TAG, programState.modalEvent.toString())
                }
            }
        }
    }

    // When submitList() is used to populate or update the recyclerView list,
    // the ListAdapter calculates the diffs internally without having to manually update and notifyDataSetChanged()
    private fun populateRecyclerView(programs: List<ProgramWithWorkoutCount>) {
        programsAdapter.submitList(programs) {
            rvPrograms.scrollToPosition(0)
        }
    }

    // Called everytime ProgramState changes to Listen for the ProgramModalEvent state
    // Callback is passed in to set ModalEvent to null to do nothing after dismissal and no modalEvents
    private fun handleModalEvent(modalEvent: ProgramModalEvent?) {
        when (modalEvent) {
            is ProgramModalEvent.ShowCreateForm -> {
                val modalBottomSheet = ProgramModalBottomSheet()
                modalBottomSheet.show(parentFragmentManager, ProgramModalBottomSheet.TAG)
                modalBottomSheet.onDismissListener = {
                    programsViewModel.resetModalEvent()
                }
            }

            is ProgramModalEvent.ShowEditForm -> {
                val modalBottomSheet = ProgramModalBottomSheet()
                modalBottomSheet.show(parentFragmentManager, ProgramModalBottomSheet.TAG)
                modalBottomSheet.onDismissListener = {
                    programsViewModel.resetModalEvent()
                }
            }

            null -> {}
        }
    }

}