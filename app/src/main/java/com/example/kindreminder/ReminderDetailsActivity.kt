package com.example.kindreminder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.kindreminder.ReminderAdapter.ReminderViewHolder

class ReminderDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_details)

        val reminderName = intent.getStringExtra("REMINDER_TITLE")

        val nameTextView: TextView = findViewById(R.id.nameTextInputEditText)

        nameTextView.text = reminderName
    }

//    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
//        val reminder = reminders[position]
//        holder.nameTextInputEditText.setText(reminder.name)  // Set initial name
//        holder.timeTextView.text = formatTimestamp(reminder.time.toDate().time)
//        holder.finishedCheckBox.isChecked = reminder.finished
//
//        // Focus change listener to highlight border when focused
//        holder.nameTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                // Highlight the border on focus
//                holder.nameTextInputLayout.setBoxStrokeColor(
//                    ContextCompat.getColor(context, R.color.teal_200)
//                )
//                holder.nameTextInputLayout.setBoxStrokeWidth(3) // Thicker border on focus
//            } else {
//                // Reset the border color when focus is lost
//                holder.nameTextInputLayout.setBoxStrokeColor(
//                    ContextCompat.getColor(context, R.color.purple_700)
//                )
//                holder.nameTextInputLayout.setBoxStrokeWidth(2) // Normal border width
//            }
//        }
//    }
}
