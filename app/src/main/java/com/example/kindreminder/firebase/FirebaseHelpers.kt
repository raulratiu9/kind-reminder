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

            remindersCollection.addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    Log.e(
                        "FirestoreError",
                        "Error fetching reminders: ${exception.message}",
                        exception
                    )
                    onFailure(exception)
                    return@addSnapshotListener
                }

                if (snapshots != null && !snapshots.isEmpty) {
                    val reminders = snapshots.documents.mapNotNull { document ->
                        try {
                            val reminder = document.toObject(Reminder::class.java)
                            reminder?.id = document.id
                            reminder
                        } catch (e: Exception) {
                            Log.e(
                                "ParsingError",
                                "Error parsing reminder document: ${document.id}",
                                e
                            )
                            null
                        }
                    }
                    onSuccess(reminders)
                } else {
                    Log.d("Firestore", "No reminders found.")
                    onSuccess(emptyList())
                }
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
                    Log.e("AddReminder", "Error adding reminder", e)
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

            reminder.update(updatedReminder).addOnSuccessListener {
                Log.d("UpdateReminder", "Reminder updated with ID: $reminderId")
            }
                .addOnFailureListener { e ->
                    Log.e("UpdateReminder", "Error updating reminder", e)
                }
        }

        fun deleteReminder(
            reminderId: String,
        ) {
            val db = Firebase.firestore
            val reminder = db.collection("reminders").document(reminderId)



            reminder.delete().addOnSuccessListener {
                Log.d("DeleteReminder", "Reminder deleted with ID: $reminderId")
            }
                .addOnFailureListener { e ->
                    Log.e("DeleteReminder", "Error deleting reminder", e)
                }
        }
    }
}