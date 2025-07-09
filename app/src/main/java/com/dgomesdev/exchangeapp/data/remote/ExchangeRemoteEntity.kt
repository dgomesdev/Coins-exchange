package com.dgomesdev.exchangeapp.data.remote

import com.google.gson.annotations.SerializedName

typealias ExchangeResponse = Map<String, ExchangeValue>

typealias ExchangeRemoteEntity = Map.Entry<String, ExchangeValue>

data class ExchangeValue (
    val code: String,
    val codein: String,
    val name: String,
    val high: Double,
    val low: Double,
    val varBid: Double,
    val pctChange: Double,
    val bid: Double,
    val ask: Double,
    val timestamp: Int,
    @SerializedName("create_date")
    val createDate: String
    )