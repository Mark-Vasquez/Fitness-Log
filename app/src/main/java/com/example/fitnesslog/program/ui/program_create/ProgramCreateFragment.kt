package com.example.fitnesslog.program.ui.program_create

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.fitnesslog.R
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.SCHEDULED_DAYS
import com.example.fitnesslog.core.utils.showDiscardDialog
import com.example.fitnesslog.databinding.FragmentProgramCreateBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class ProgramCreateFragment : Fragment() {

    private val programCreateViewModel: ProgramCreateViewModel by viewModels { ProgramCreateViewModel.Factory }
    private var _binding: FragmentProgramCreateBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "ProgramCreateFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackButtonListener()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProgramCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()


        binding.btnCancelProgramCreate.setOnClickListener {
            showDiscardDialog(
                requireContext()
            ) {
                programCreateViewModel.onEvent(ProgramCreateEvent.Cancel)
                findNavController().popBackStack()
            }
        }

        binding.btnSaveProgramCreate.setOnClickListener {
            programCreateViewModel.onEvent(ProgramCreateEvent.Save)
            findNavController().popBackStack()
        }

        binding.btnScheduleProgramCreate.setOnClickListener {
            handleModalResult<Set<Day>>(
                R.id.programCreateFragment,
                SCHEDULED_DAYS
            ) { scheduleDays ->
                binding.tvScheduledDaysProgramCreate.text = scheduleDays.toString()
            }


            val action =
                ProgramCreateFragmentDirections.actionProgramCreateFragmentToScheduleSelectModal()
            findNavController().navigate(action)
        }

        binding.etNameProgramCreate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val name = binding.etNameProgramCreate.text.toString()
                programCreateViewModel.updateProgramName(name)
            }
        })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                programCreateViewModel.stateFlow.collect { programCreateState ->
                    programCreateState.error?.let {
                        Snackbar.make(
                            requireView(),
                            it,
                            Snackbar.LENGTH_LONG
                        )
                    }
                }
            }
        }
    }

    private fun <T> handleModalResult(
        currDestination: Int,
        savedStateHandleKey: String,
        handleResult: (T?) -> Unit
    ) {
        /** NavBackStackEntry is a lifecycleOwner reference to a destination on the nav backstack
         *  that provides a lifecycle, viewmodelStore, and savedStateHandle. Allows the navBackStack
         *  to manage the same viewModel and state in memory that the destination fragment owns
         */
        val navBackStackEntry =
            findNavController().getBackStackEntry(currDestination)

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME
                && navBackStackEntry.savedStateHandle.contains(savedStateHandleKey)
            ) {
                val result = navBackStackEntry.savedStateHandle.get<T>(savedStateHandleKey);
                // Do something with the result
                handleResult(result)
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }

    private fun setBackButtonListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            showDiscardDialog(
                requireContext()
            ) {
                programCreateViewModel.onEvent(
                    ProgramCreateEvent.Cancel
                )
                findNavController().popBackStack()
            }


        }
    }
}