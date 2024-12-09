package com.example.kindreminder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kindreminder.classes.Reminder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ReminderAdapter(private val context: Context, private val reminders: List<Reminder>) :
    RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    // ViewHolder class to hold the views for each item
    class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        val finishedCheckBox: CheckBox = view.findViewById(R.id.finishedCheckBox)
    }

    fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    // Inflate the reminder item layout and create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminder_item, parent, false)
        return ReminderViewHolder(view)
    }

    // Bind the data to each ViewHolder
    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        holder.nameTextView.text = reminder.name
        holder.timeTextView.text = formatTimestamp(reminder.time.toDate().time)
        holder.finishedCheckBox.isChecked = reminder.finished
    }

    // Return the size of the list
    override fun getItemCount(): Int = reminders.size
}
