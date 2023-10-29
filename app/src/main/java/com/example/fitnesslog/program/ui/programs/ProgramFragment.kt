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
import com.example.fitnesslog.R
import com.example.fitnesslog.core.ui.viewModelFactoryHelper
import com.example.fitnesslog.core.utils.GridSpacingItemDecoration
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProgramFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProgramFragment : Fragment() {

    private lateinit var viewModel: ProgramsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val programsViewModelFactory =
            viewModelFactoryHelper { ProgramsViewModel(programModule.programUseCases) }
        viewModel =
            ViewModelProvider(this, programsViewModelFactory)[ProgramsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_program, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect {
                    val programs = it.programs
                    val programAdapter = ProgramsAdapter(programs)
                    val rvPrograms: RecyclerView = view.findViewById(R.id.rvPrograms)
                    rvPrograms.adapter = programAdapter
                    rvPrograms.layoutManager = GridLayoutManager(context, 2)
                    rvPrograms.addItemDecoration(GridSpacingItemDecoration(20))
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
            ProgramFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}