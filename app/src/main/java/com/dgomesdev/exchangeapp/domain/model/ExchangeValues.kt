package com.dgomesdev.exchangeapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

typealias ExchangeResponse = HashMap<String, ExchangeValues>

@Entity(tableName = "coin_table")
data class ExchangeValues (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val code: String,
    val codein: String,
    val name: String,
    val bid: Double,
    val convertedAmount: Double
)
