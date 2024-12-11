package com.example.kindreminder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kindreminder.firebase.FirebaseHelpers
import com.example.kindreminder.ui.SwipeToDeleteCallback
import com.example.kindreminder.ui.SwipeToEditCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.messaging

class MainActivity : AppCompatActivity() {

    private lateinit var remindersRecyclerView: RecyclerView
    private lateinit var adapter: ReminderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)
        val addButton: View = findViewById(R.id.btnAdd)

        addButton.setOnClickListener {
            val intent = Intent(this, AddReminderActivity::class.java)
            startActivity(intent)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCMM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = token
            Log.d("FCMM", msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        // Initialize the RecyclerView
        remindersRecyclerView = findViewById(R.id.remindersRecyclerView)
        remindersRecyclerView.layoutManager = LinearLayoutManager(this)

// Subscribe to a campaign
        Firebase.messaging.subscribeToTopic("test")
            .addOnCompleteListener { task ->
                var msg = "Subscribed to Campaign A"
                if (!task.isSuccessful) {
                    msg = "Subscription to Campaign A failed"
                }
                Log.d("FCM", msg)
            }

// Subscribe to another campaign
        Firebase.messaging.subscribeToTopic("kind-reminder")
            .addOnCompleteListener { task ->
                var msg = "Subscribed to Campaign B"
                if (!task.isSuccessful) {
                    msg = "Subscription to Campaign B failed"
                }
                Log.d("FCM", msg)
            }

        Firebase.messaging.subscribeToTopic("weather")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d("merge", msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }


        // Fetch reminders from Firestore
        FirebaseHelpers.getReminders(
            onSuccess = { reminders ->
                // Set the adapter with the fetched reminders
                adapter = ReminderAdapter(this, reminders)
                remindersRecyclerView.adapter = adapter
                adapter.updateReminders(reminders)
                val swipeToEditCallback = SwipeToEditCallback(this, adapter)
                val swipeToDeleteCallback = SwipeToDeleteCallback(this, adapter)
                val editTouchHelper = ItemTouchHelper(swipeToEditCallback)
                val deleteTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                editTouchHelper.attachToRecyclerView(remindersRecyclerView)
                deleteTouchHelper.attachToRecyclerView(remindersRecyclerView)
            },
            onFailure = { exception ->
                Log.e("HomeActivity", "Failed to fetch reminders: ${exception.message}")
            }
        )
    }

}
