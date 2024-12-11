package com.example.kindreminder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
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


class ReminderAdapter(private val context: Context, private var reminders: List<Reminder>) :
    RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        val finishedCheckBox: CheckBox = view.findViewById(R.id.finishedCheckBox)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateReminders(newReminders: List<Reminder>) {
        reminders = newReminders
        notifyDataSetChanged()
    }

    private fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminder_item, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]

        holder.nameTextView.text = reminder.name
        holder.timeTextView.text = formatTimestamp(reminder.time.toDate().time)
        holder.finishedCheckBox.isChecked = reminder.finished

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ReminderDetailsActivity::class.java).apply {
                putExtra("REMINDER_ID", reminder.id)
                putExtra("REMINDER_TITLE", reminder.name)
                putExtra("REMINDER_TIME", reminder.time.toDate())
                putExtra("REMINDER_FINISHED", reminder.finished)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = reminders.size

    fun getReminderAtPosition(position: Int): Reminder {
        return reminders[position]
    }


}
