package com.dgomesdev.exchangeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_updated_timestamp")
data class LastUpdatedTimestamp(
    @PrimaryKey
    val id: Int = 0,
    val timestamp: Long
)
