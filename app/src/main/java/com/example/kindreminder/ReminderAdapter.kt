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
import com.example.kindreminder.firebase.FirebaseHelpers
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ReminderAdapter(private val context: Context, private var reminders: List<Reminder>) :
    RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>() {

    // ViewHolder class to hold the views for each item
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

    // Inflate the reminder item layout and create the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reminder_item, parent, false)
        return ReminderViewHolder(view)
    }

    // Bind the data to each ViewHolder
    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = reminders[position]
        Log.d("Reminder", reminder.toString())
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

    // Return the size of the list
    override fun getItemCount(): Int = reminders.size

    fun getReminderAtPosition(position: Int): Reminder {
        return reminders[position]
    }


}
