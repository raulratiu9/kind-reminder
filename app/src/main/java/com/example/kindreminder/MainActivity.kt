package com.example.kindreminder

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kindreminder.firebase.FirebaseHelpers
import com.example.kindreminder.ui.SwipeToDeleteCallback
import com.example.kindreminder.ui.SwipeToEditCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp

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

        // Initialize the RecyclerView
        remindersRecyclerView = findViewById(R.id.remindersRecyclerView)
        remindersRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch reminders from Firestore
        FirebaseHelpers.getReminders(
            onSuccess = { reminders ->
                // Set the adapter with the fetched reminders
                adapter = ReminderAdapter(this, reminders)
                remindersRecyclerView.adapter = adapter

// Attach ItemTouchHelper for swipe-to-edit
                val swipeToEditCallback = SwipeToEditCallback(this, adapter)
                val swipeToDeleteCallback = SwipeToDeleteCallback(this, adapter)
                val editTouchHelper = ItemTouchHelper(swipeToEditCallback)
                val deleteTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                editTouchHelper.attachToRecyclerView(remindersRecyclerView)
                deleteTouchHelper.attachToRecyclerView(remindersRecyclerView)
            },
            onFailure = { exception ->
                // Handle the error case
                // Optionally, show a message or handle the failure
                // For example: showToast("Error: ${exception.message}")
            }
        )
    }
}
