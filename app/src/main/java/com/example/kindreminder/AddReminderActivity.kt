package com.example.kindreminder

import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.kindreminder.firebase.FirebaseHelpers
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import java.util.Calendar

class AddReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        val reminderNameEditText = findViewById<TextInputEditText>(R.id.etReminderName)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        val addReminderButton = findViewById<Button>(R.id.btnAddReminder)

        addReminderButton.setOnClickListener {
            val reminderName = reminderNameEditText.text.toString()

            if (reminderName.isBlank()) {
                reminderNameEditText.error = "Please enter a reminder name"
                return@setOnClickListener
            }

            val hour = timePicker.hour
            val minute = timePicker.minute

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)

            val reminderTime = Timestamp(calendar.time)

            FirebaseHelpers.addReminder(reminderName, reminderTime)
            finish()
        }
    }
}
