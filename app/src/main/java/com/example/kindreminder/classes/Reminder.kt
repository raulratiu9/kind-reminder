package com.example.kindreminder.classes

import com.google.firebase.Timestamp

data class Reminder(
    var id: String = "",
    val name: String = "",
    val time: Timestamp = Timestamp.now(),
    val finished: Boolean = false
)