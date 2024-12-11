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

class SwipeToEditCallback(
    private val context: Context,
    private val adapter: ReminderAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val reminder = adapter.getReminderAtPosition(position)

        val intent = Intent(context, ReminderDetailsActivity::class.java).apply {
            putExtra("REMINDER_ID", reminder.id)
            putExtra("REMINDER_TITLE", reminder.name)
            putExtra("REMINDER_TIME", reminder.time.toDate())
            putExtra("REMINDER_FINISHED", reminder.finished)
        }
        context.startActivity(intent)
        adapter.notifyItemChanged(position)
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

        paint.color = Color.YELLOW
        c.drawRect(
            itemView.left.toFloat(),
            itemView.top.toFloat(),
            itemView.left + dX,
            itemView.bottom.toFloat(),
            paint
        )

        val icon =
            ContextCompat.getDrawable(context, R.drawable.ic_edit)
        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + iconMargin
        val iconBottom = iconTop + icon.intrinsicHeight
        val iconLeft = itemView.left + iconMargin
        val iconRight = iconLeft + icon.intrinsicWidth

        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
