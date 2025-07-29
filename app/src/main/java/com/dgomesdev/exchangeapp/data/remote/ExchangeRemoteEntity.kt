package com.dgomesdev.exchangeapp.data.remote

import com.google.gson.annotations.SerializedName

typealias ExchangeResponse = Map<String, ExchangeValue>
typealias ExchangeRemoteEntity = Map.Entry<String, ExchangeValue>

data class ExchangeValue (
    val code: String,
    val codein: String,
    val name: String,
    val high: String,
    val low: String,
    val varBid: String,
    val pctChange: String,
    val bid: String,
    val ask: String,
    val timestamp: String,
    @SerializedName("create_date")
    val createDate: String
    )