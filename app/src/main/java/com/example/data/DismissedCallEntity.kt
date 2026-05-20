package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dismissed_calls")
data class DismissedCallEntity(
    @PrimaryKey val idString: String, // phoneNumber + "_" + timestamp
    val phoneNumber: String,
    val timestamp: Long
)
