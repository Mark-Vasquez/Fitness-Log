package com.example.fitnesslog.program.ui.programs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.fitnesslog.R
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
                    if (newState == BottomSheetBehavior.STATE_DRAGGING && !discardDialogShown) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        showDiscardDialog(requireContext(), this@ProgramModalBottomSheet) {
                            discardDialogShown = false
                            behavior.isDraggable = true
                        }
                        discardDialogShown = true
                        behavior.isDraggable = false
                    }
                    Log.d(TAG, "onStateChanged $newState $discardDialogShown")
                    return
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    return
                }
            }
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
        configureBottomSheetViews()

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


    private fun configureBottomSheetViews() {
        val bottomSheetDialog = dialog as BottomSheetDialog
        val bottomSheetContainer =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout

        bottomSheetContainer.minimumHeight = (resources.displayMetrics.heightPixels)
        val bottomSheetBehavior = bottomSheetDialog.behavior
        bottomSheetBehavior.apply {
            isFitToContents = false
            isShouldRemoveExpandedCorners = false
            expandedOffset = resources.getInteger(R.integer.offset_from_top)
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
}
