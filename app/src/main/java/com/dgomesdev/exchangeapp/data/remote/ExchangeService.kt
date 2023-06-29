package com.dgomesdev.exchangeapp.data.remote

import com.dgomesdev.exchangeapp.domain.model.ExchangeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeService {

    @GET("/json/last/{coins}")
    suspend fun getExchangeValues(@Path("coins") coins: String): ExchangeResponse
}