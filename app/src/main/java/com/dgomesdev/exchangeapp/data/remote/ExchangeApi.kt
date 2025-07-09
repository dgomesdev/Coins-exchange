package com.dgomesdev.exchangeapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Path


interface ExchangeApi {

    @GET("/json/last/{coins}")
    suspend fun getExchangeValues(@Path("coins") coins: String): ExchangeResponse
}