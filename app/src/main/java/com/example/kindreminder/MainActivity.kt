package com.example.kindreminder

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kindreminder.firebase.FirebaseHelpers
import com.example.kindreminder.ui.SwipeToDeleteCallback
import com.example.kindreminder.ui.SwipeToEditCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
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
                Log.e("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            Log.d("FCM", token)
        })

        Firebase.messaging.subscribeToTopic("test")
            .addOnCompleteListener { task ->
                var msg = "Subscribed to Campaign A"
                if (!task.isSuccessful) {
                    msg = "Subscription to Campaign A failed"
                }
                Log.d("FCM", msg)
            }

        Firebase.messaging.subscribeToTopic("kind-reminder")
            .addOnCompleteListener { task ->
                var msg = "Subscribed to Campaign B"
                if (!task.isSuccessful) {
                    msg = "Subscription to Campaign B failed"
                }
                Log.d("FCM", msg)
            }

        remindersRecyclerView = findViewById(R.id.remindersRecyclerView)
        remindersRecyclerView.layoutManager = LinearLayoutManager(this)

        FirebaseHelpers.getReminders(
            onSuccess = { reminders ->
                adapter = ReminderAdapter(this, reminders)
                remindersRecyclerView.adapter = adapter

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        // Refresh reminders and notify adapter
        FirebaseHelpers.getReminders(
            onSuccess = { reminders ->
                adapter.updateReminders(reminders)
            },
            onFailure = { exception ->
                Log.e("HomeActivity", "Failed to fetch reminders: ${exception.message}")
            }
        )

        // Reset swipe state by refreshing the RecyclerView
        remindersRecyclerView.adapter?.notifyDataSetChanged()
    }


}
