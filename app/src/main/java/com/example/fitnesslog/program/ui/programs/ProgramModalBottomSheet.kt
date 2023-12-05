package com.example.fitnesslog.program.ui.programs

import android.content.DialogInterface
import android.os.Bundle
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
        configureBottomSheet()

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

    var onDismissListener: (() -> Unit)? = null

    private fun configureBottomSheet() {
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
