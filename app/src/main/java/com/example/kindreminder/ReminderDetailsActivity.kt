package com.example.kindreminder

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReminderDetailsActivity : AppCompatActivity() {

    private lateinit var nameTextInputLayout: TextInputLayout
    private lateinit var nameTextInputEditText: EditText
    private lateinit var reminderDetailTextView: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_details)

        nameTextInputLayout = findViewById(R.id.nameTextInputLayout)
        nameTextInputEditText = findViewById(R.id.nameTextInputEditText)
        reminderDetailTextView = findViewById(R.id.reminderDetailTextView)
        val rootLayout = findViewById<LinearLayout>(R.id.rootLayout)

        val reminderId = intent.getStringExtra("REMINDER_ID") ?: "No Name"
        val reminderName = intent.getStringExtra("REMINDER_TITLE") ?: "No Name"
        val reminderTime = intent.getLongExtra("REMINDER_TIME", -1)

        nameTextInputEditText.setText(reminderName)
        reminderDetailTextView.text = formatTimestamp(reminderTime)

        rootLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (!isTouchInsideView(nameTextInputEditText, event)) {
                    nameTextInputEditText.clearFocus()
                }
            }
            false
        }

        nameTextInputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val updatedName = nameTextInputEditText.text.toString()

                FirebaseHelpers.editReminder(
                    reminderId,
                    updatedName,
                    Timestamp(Date(reminderTime)),
                    false
                )
            } else {
                nameTextInputLayout.boxStrokeColor = ContextCompat.getColor(this, R.color.teal_200)
            }
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val formatter = SimpleDateFormat("EEEE, MMMM d HH:mm", Locale.getDefault())
        return formatter.format(Date(timestamp))
    }

    private fun isTouchInsideView(view: View, event: MotionEvent): Boolean {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)
        return rect.contains(event.rawX.toInt(), event.rawY.toInt())
    }
}
