package com.dgomesdev.exchangeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_table")
data class ExchangeLocalEntity(
    @PrimaryKey
    val conversionPair: String,
    val name: String,
    val bid: Double,
    val code: String
)