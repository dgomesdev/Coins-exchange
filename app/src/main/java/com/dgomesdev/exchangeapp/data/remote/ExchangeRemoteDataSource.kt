package com.dgomesdev.exchangeapp.data.remote

interface ExchangeRemoteDataSource {
    suspend fun getExchangeValues(coins: String): ExchangeResponse
}