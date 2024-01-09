package com.example.fitnesslog.program.ui.programs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.example.fitnesslog.core.enums.Day
import com.example.fitnesslog.core.utils.showDiscardDialog
import com.example.fitnesslog.databinding.ModalBottomSheetProgramBinding
import com.example.fitnesslog.program.data.entity.Program
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProgramModalBottomSheet : BottomSheetDialogFragment() {

    // Lazily delegates the value provision to the viewModels() function, which retrieves the
    // ProgramsViewModel instance created by the parent fragment (ProgramsFragment), sharing the same view model
    private val programsViewModel: ProgramsViewModel by viewModels(
        ownerProducer = { requireParentFragment() },
        factoryProducer = { ProgramsViewModel.Factory })
    var onDismissListener: (() -> Unit)? = null
    private var _binding: ModalBottomSheetProgramBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "ProgramModalBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ModalBottomSheetProgramBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureBottomSheetView()

        binding.tvProgramModalCancel.setOnClickListener {
            showDiscardDialog(requireContext(), this)
        }

        binding.tvProgramModalSave.setOnClickListener {
            saveProgramData()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            val behavior = dialog.behavior
            val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    return
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (slideOffset < 0.2) {
                        showDiscardDialog(requireContext(), this@ProgramModalBottomSheet) {
                            behavior.isDraggable = true
                        }
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        behavior.isDraggable = false
                    }
                    return
                }
            }
            behavior.addBottomSheetCallback(bottomSheetCallback)
        }
        return dialog
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Releases the ViewBinding instance's references to the ViewObject instance destroyed
        // Garbage collection reclaims memory from both instances
        _binding = null
    }


    private fun saveProgramData() {
        // TODO: check whether save on edit or create
        val name = binding.etProgramName.toString()
        val program = Program(
            name = name,
            scheduledDays = getSelectedDays(),
            restDurationSeconds = 90,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        programsViewModel.onEvent(ProgramsEvent.Create(program))
    }

    private fun getSelectedDays(): Set<Day> {
        val selectedDays = mutableSetOf<Day>()

        binding.run {
            if (chipMonday.isChecked) selectedDays.add(Day.MONDAY)
            if (chipTuesday.isChecked) selectedDays.add(Day.TUESDAY)
            if (chipWednesday.isChecked) selectedDays.add(Day.WEDNESDAY)
            if (chipThursday.isChecked) selectedDays.add(Day.THURSDAY)
            if (chipFriday.isChecked) selectedDays.add(Day.FRIDAY)
            if (chipSaturday.isChecked) selectedDays.add(Day.SATURDAY)
            if (chipSunday.isChecked) selectedDays.add(Day.SUNDAY)
        }
        return selectedDays
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun configureBottomSheetView() {
        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.setCanceledOnTouchOutside(false)

        val bottomSheetContainer =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        val layoutParams = bottomSheetContainer.layoutParams
        layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT

        bottomSheetContainer.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val currentFocusView = view.findFocus()
                if (currentFocusView is EditText) {
                    val outRect = Rect()
                    currentFocusView.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        currentFocusView.clearFocus()
                        val inputMethodManager =
                            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(currentFocusView.windowToken, 0)
                    }
                }
            }
            false
        }

        val bottomSheetBehavior = bottomSheetDialog.behavior
        bottomSheetBehavior.apply {
            isFitToContents = false
            isShouldRemoveExpandedCorners = false
            halfExpandedRatio = 0.001F
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun getStateName(state: Int): String {
        return when (state) {
            BottomSheetBehavior.STATE_EXPANDED -> "STATE_EXPANDED"
            BottomSheetBehavior.STATE_COLLAPSED -> "STATE_COLLAPSED"
            BottomSheetBehavior.STATE_DRAGGING -> "STATE_DRAGGING"
            BottomSheetBehavior.STATE_SETTLING -> "STATE_SETTLING"
            BottomSheetBehavior.STATE_HIDDEN -> "STATE_HIDDEN"
            BottomSheetBehavior.STATE_HALF_EXPANDED -> "STATE_HALF_EXPANDED"
            else -> "UNKNOWN_STATE"
        }
    }

}