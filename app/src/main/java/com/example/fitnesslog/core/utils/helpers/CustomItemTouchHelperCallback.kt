package com.example.fitnesslog.core.utils.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnesslog.R
import com.google.android.material.color.MaterialColors
import kotlin.math.abs

abstract class CustomItemTouchHelperCallback(
    context: Context,
    private val dragDirections: Int,
    private val swipeDirections: Int
) :
    ItemTouchHelper.Callback() {

    private val deleteIconDrawable =
        ContextCompat.getDrawable(context, R.drawable.delete_24px)?.mutate()
    private val deleteIconColor = MaterialColors.getColor(
        context,
        com.google.android.material.R.attr.colorOnErrorContainer,
        Color.WHITE
    )

    private val deleteBackgroundColor = MaterialColors.getColor(
        context,
        com.google.android.material.R.attr.colorErrorContainer,
        Color.parseColor("#f44336")
    )
    private val deleteBackgroundDrawable = ColorDrawable(deleteBackgroundColor)

    init {
        // Ensure the drawable is not null before applying the tint
        deleteIconDrawable?.let {
            DrawableCompat.wrap(it)
            DrawableCompat.setTint(it, deleteIconColor)
        }
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder.adapterPosition == 0 && dragDirections == 0) {
            makeMovementFlags(dragDirections, 0)
        } else {
            makeMovementFlags(dragDirections, swipeDirections)
        }

    }

    abstract override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean


    abstract override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int)

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCancelled = dX == 0f && !isCurrentlyActive

        // When a user doesn't swipe all the way and it springs back
        if (isCancelled || deleteIconDrawable == null) {
            super.onChildDraw(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
            return
        }

        // Sets the red delete background position to show only how far dX swiped left from right
        deleteBackgroundDrawable.setBounds(
            itemView.right + dX.toInt(),
            itemView.top,
            itemView.right,
            itemView.bottom
        )
        deleteBackgroundDrawable.draw(c)

        // Get the position values for the delete icon bounds
        val iconMargin = (itemHeight - deleteIconDrawable.intrinsicHeight) / 2
        val iconTop = itemView.top + iconMargin
        val iconLeft = itemView.right - iconMargin - deleteIconDrawable.intrinsicWidth
        val iconRight = itemView.right - iconMargin
        val iconBottom = itemView.bottom - iconMargin

        // Only show icon if background is big enough to fit the icon
        if (abs(dX) > abs(iconRight - iconLeft) + (iconMargin)) {
            deleteIconDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            deleteIconDrawable.draw(c)
        }

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }


    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.alpha = 0.5f
        }
    }

    abstract override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    )


}