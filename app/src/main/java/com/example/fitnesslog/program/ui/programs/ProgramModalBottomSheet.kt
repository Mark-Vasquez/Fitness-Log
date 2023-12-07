package com.example.fitnesslog.program.ui.programs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.fitnesslog.R
import com.example.fitnesslog.core.utils.dpToPx
import com.example.fitnesslog.core.utils.showDiscardDialog
import com.example.fitnesslog.databinding.ModalBottomSheetProgramBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProgramModalBottomSheet : BottomSheetDialogFragment() {

    var onDismissListener: (() -> Unit)? = null
    var discardDialogShown = false
    private var _binding: ModalBottomSheetProgramBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "ProgramModalBottomSheet"
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog


        dialog.setOnShowListener {
            val behavior = dialog.behavior
            val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }, 1000)
                    }
//                    if (newState == BottomSheetBehavior.STATE_DRAGGING && !discardDialogShown) {
//                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//                        showDiscardDialog(requireContext(), this@ProgramModalBottomSheet) {
//                            discardDialogShown = false
//                            behavior.isDraggable = true
//                        }
//                        discardDialogShown = true
//                        behavior.isDraggable = false
//                    }
                    Log.d(
                        TAG,
                        "onStateChanged ${getStateName(newState)} peekHeight: ${behavior.peekHeight}, maxHeight: ${behavior.maxHeight}"
                    )

                    return
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val maxSheetHeight = bottomSheet.height
                    val currentHeight = maxSheetHeight * slideOffset
                    Log.d(
                        TAG,
                        "CurrentHeight: $currentHeight, SlidOffset $slideOffset"
                    )
                    return
                }
            }
            behavior.setPeekHeight(1175, false)
            behavior.addBottomSheetCallback(bottomSheetCallback)
        }
        return dialog
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

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke()

    }


    private fun configureBottomSheetView() {
        val bottomSheetDialog = dialog as BottomSheetDialog
        val bottomSheetContainer =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout

        val layoutParams = bottomSheetContainer.layoutParams
        layoutParams.height = FrameLayout.LayoutParams.MATCH_PARENT
        val bottomSheetBehavior = bottomSheetDialog.behavior
        bottomSheetBehavior.apply {
            isFitToContents = false
            isShouldRemoveExpandedCorners = false
            expandedOffset = dpToPx(resources.getInteger(R.integer.offset_from_top), resources)
            halfExpandedRatio = 0.01F
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        Log.d(TAG, "starting state: ${getStateName(bottomSheetBehavior.state)}")
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