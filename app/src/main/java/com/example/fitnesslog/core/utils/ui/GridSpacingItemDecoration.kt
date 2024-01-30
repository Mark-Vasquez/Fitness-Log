package com.example.fitnesslog.core.utils.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Adding margin manually to each items is possibly inefficient for large datasets because the
 * recyclerView handle more complex views every time it recycles. Adding a decoration is fixed around
 * each individual item. Manual margin can also double up the margin in between two items, which gave
 * me uneven spacing everywhere .The layoutManager takes into account this decoration and calculates
 * consistent spacing between items.
 */
class GridSpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(spacing, spacing, spacing, spacing)
    }
}