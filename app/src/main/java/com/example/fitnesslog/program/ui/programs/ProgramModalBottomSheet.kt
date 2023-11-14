package com.example.fitnesslog.program.ui.programs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitnesslog.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProgramModalBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.modal_bottom_sheet_program, container, false)
}
