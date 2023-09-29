package com.dgomesdev.exchangeapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

typealias ExchangeResponse = HashMap<String, ExchangeValues>

@Entity(tableName = "coin_table")
data class ExchangeValues (
    @PrimaryKey
    val name: String,
    val bid: Double,
    @SerializedName("create_date")
    val createDate: String
)
