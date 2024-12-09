package com.example.kindreminder

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.kindreminder.firebase.FirebaseHelpers
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import java.util.Calendar

class AddReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        // Get references to the views
        val reminderNameEditText = findViewById<TextInputEditText>(R.id.etReminderName)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val addReminderButton = findViewById<Button>(R.id.btnAddReminder)

        // Set the click listener on the Add Reminder button
        addReminderButton.setOnClickListener {
            val reminderName = reminderNameEditText.text.toString()

            // Validate the name
            if (reminderName.isBlank()) {
                reminderNameEditText.error = "Please enter a reminder name"
                return@setOnClickListener
            }

            // Get the time selected in the TimePicker
            val hour = timePicker.hour
            val minute = timePicker.minute

            // Create a Calendar object to set the time as a Timestamp
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            // Create a Timestamp object from the calendar
            val reminderTime = Timestamp(calendar.time)

            // Call the addReminder function to add the reminder
            FirebaseHelpers.addReminder(reminderName, reminderTime)
            
            finish()
        }
    }
}
