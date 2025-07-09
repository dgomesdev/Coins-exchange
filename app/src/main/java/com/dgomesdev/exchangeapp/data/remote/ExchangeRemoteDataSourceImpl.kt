package com.dgomesdev.exchangeapp.data.remote

import javax.inject.Inject

class ExchangeRemoteDataSourceImpl @Inject constructor(
    private val api: ExchangeApi
) : ExchangeRemoteDataSource {
    override suspend fun getExchangeValues(coins: String): ExchangeResponse =
        this.api.getExchangeValues(coins)
}