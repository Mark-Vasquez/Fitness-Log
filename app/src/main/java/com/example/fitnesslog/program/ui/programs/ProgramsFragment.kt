package com.example.fitnesslog.program.ui.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.FitnessLogApp.Companion.programModule
import com.example.fitnesslog.FitnessLogApp.Companion.sharedModule
import com.example.fitnesslog.R
import com.example.fitnesslog.core.ui.viewModelFactoryHelper
import com.example.fitnesslog.core.utils.GridSpacingItemDecoration
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

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var programsViewModel: ProgramsViewModel
    private lateinit var programsAdapter: ProgramsAdapter
    private lateinit var rvPrograms: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedViewModelFactory =
            viewModelFactoryHelper { SharedViewModel(sharedModule.sharedUseCases) }
        sharedViewModel =
            ViewModelProvider(
                requireActivity(),
                sharedViewModelFactory
            )[SharedViewModel::class.java]

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
    ): View? {
        return inflater.inflate(R.layout.fragment_program, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        getPrograms()
        val modalBottomSheet = ProgramModalBottomSheet()
        modalBottomSheet.show(parentFragmentManager, ProgramModalBottomSheet.TAG)
    }

    private fun setupRecyclerView(view: View) {
        rvPrograms = view.findViewById(R.id.rvPrograms)
        rvPrograms.layoutManager = GridLayoutManager(context, 2)
        rvPrograms.addItemDecoration(GridSpacingItemDecoration(25))

        programsAdapter =
            ProgramsAdapter(object : ProgramsAdapter.ProgramClickListener {
                override fun onProgramClicked(program: ProgramWithWorkoutCount) {
                    sharedViewModel.onEvent(SharedEvent.SelectProgram(program))
                }
            })
        rvPrograms.adapter = programsAdapter
    }

    private fun getPrograms() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programsViewModel.stateFlow.collect { programState ->
                    programsAdapter.submitList(programState.programs) {
                        rvPrograms.scrollToPosition(0)
                    }

                }
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProgramFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProgramsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}