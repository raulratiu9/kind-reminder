package com.example.kindreminder

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kindreminder.firebase.FirebaseHelpers
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Timestamp
import com.google.firebase.firestore.util.Logger
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReminderDetailsActivity : AppCompatActivity() {

    private lateinit var nameTextInputLayout: TextInputLayout
    private lateinit var nameTextInputEditText: EditText
    private lateinit var reminderDetailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_details)

        // Initialize views
        nameTextInputLayout = findViewById(R.id.nameTextInputLayout)
        nameTextInputEditText = findViewById(R.id.nameTextInputEditText)
        reminderDetailTextView = findViewById(R.id.reminderDetailTextView)
        val rootLayout = findViewById<LinearLayout>(R.id.rootLayout)

        // Get the reminder data passed from the previous activity
        val reminderId = intent.getStringExtra("REMINDER_ID") ?: "No Name"
        val reminderName = intent.getStringExtra("REMINDER_TITLE") ?: "No Name"
        val reminderTime = intent.getLongExtra("REMINDER_TIME", -1)

        // Set the data to the views
        nameTextInputEditText.setText(reminderName)
        reminderDetailTextView.text = formatTimestamp(reminderTime)

        rootLayout.setOnTouchListener { v, event ->
            // Check if the touch event occurred outside of the input field
            if (event.action == MotionEvent.ACTION_DOWN) {
                // If the touch is outside of the TextInputEditText, clear the focus
                if (!isTouchInsideView(nameTextInputEditText, event)) {
                    nameTextInputEditText.clearFocus() // This will cause the input field to blur
                }
            }
            false // Return false to allow other touch events to happen
        }

        // Listen for focus changes on the EditText
        nameTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val updatedName = nameTextInputEditText.text.toString()
                Log.d(
                    "UpdateReminder",
                    reminderId,
                )
                Log.d(
                    "UpdateReminder",
                    (updatedName != reminderName).toString()
                )
                FirebaseHelpers.editReminder(
                    reminderId,
                    updatedName,
                    Timestamp(Date(reminderTime)),
                    false
                )
                Log.w("Successfully edited", "bine bos")


            } else {
                nameTextInputLayout.setBoxStrokeColor(
                    ContextCompat.getColor(this, R.color.teal_200)  // Change color when focused
                )
            }
        }
    }

    fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("EEEE, MMMM d HH:mm", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    fun isTouchInsideView(view: View, event: MotionEvent): Boolean {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)
        return rect.contains(event.rawX.toInt(), event.rawY.toInt())
    }

    // Optional: Save the edited name when leaving the activity
    override fun onPause() {
        super.onPause()
        val editedName = nameTextInputEditText.text.toString()
        // Handle saving editedName to your database (e.g., Firebase)
    }
}
