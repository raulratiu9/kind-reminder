package com.example.kindreminder.firebase

import android.util.Log
import com.example.kindreminder.classes.Reminder
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FirebaseHelpers {
    companion object {
        fun getReminders(
            onSuccess: (List<Reminder>) -> Unit,
            onFailure: (Exception) -> Unit
        ) {
            val db = Firebase.firestore
            val remindersCollection = db.collection("reminders")

            remindersCollection.get()
                .addOnSuccessListener { documents ->
                    val reminders = documents.map { document ->
                        val reminder = document.toObject(Reminder::class.java)
                        reminder.id = document.id
                        reminder
                    }

                    Log.d("OAU", reminders.toString())
                    onSuccess(reminders)
                }

                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        }

        fun addReminder(name: String, time: Timestamp, finished: Boolean = false) {
            val db = Firebase.firestore
            val remindersCollection = db.collection("reminders")

            val newReminder = hashMapOf(
                "name" to name,
                "time" to time,
                "finished" to finished,
                "id" to FirebaseFirestore.getInstance().collection("reminders").document().id
            )

            remindersCollection.add(newReminder).addOnSuccessListener { documentReference ->
                Log.d("AddReminder", "Reminder added with ID: ${documentReference.id}")
            }
                .addOnFailureListener { e ->
                    Log.w("AddReminder", "Error adding reminder", e)
                }
        }

        fun editReminder(
            reminderId: String,
            name: String,
            time: Timestamp,
            finished: Boolean = false
        ) {
            val db = Firebase.firestore
            val reminder = db.collection("reminders").document(reminderId)

            val updatedReminder = mapOf(
                "name" to name,
                "time" to time,
                "finished" to finished,
                "id" to reminderId
            )

            reminder.update(updatedReminder).addOnSuccessListener { documentReference ->
                Log.d("UpdateReminder", "Reminder updated with ID: ${reminderId}")
            }
                .addOnFailureListener { e ->
                    Log.w("UpdateReminder", "Error updating reminder", e)
                }
        }
    }
}