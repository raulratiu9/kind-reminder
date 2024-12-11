package com.example.kindreminder.ui

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.kindreminder.R
import com.example.kindreminder.ReminderAdapter
import com.example.kindreminder.ReminderDetailsActivity
import com.example.kindreminder.firebase.FirebaseHelpers

class SwipeToDeleteCallback(
    private val context: Context,
    private val adapter: ReminderAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // No need to handle move actions
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val reminder = adapter.getReminderAtPosition(position)

        FirebaseHelpers.deleteReminder(reminder.id)
        // Reset the swipe action

        adapter.notifyItemRemoved(position)
    }

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
        val paint = Paint()

        // Yellow background for swipe
        paint.color = Color.RED
        c.drawRect(
            itemView.right.toFloat(),
            itemView.top.toFloat(),
            itemView.right + dX,
            itemView.bottom.toFloat(),
            paint
        )

        // Draw an edit icon or label
        val icon =
            ContextCompat.getDrawable(context, R.drawable.ic_trash) // Replace with your edit icon
        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + iconMargin
        val iconBottom = iconTop + icon.intrinsicHeight
        val iconLeft = itemView.right + iconMargin
        val iconRight = iconLeft + icon.intrinsicWidth

        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
