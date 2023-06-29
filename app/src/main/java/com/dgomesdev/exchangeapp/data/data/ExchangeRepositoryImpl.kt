package com.dgomesdev.exchangeapp.data.data

import com.dgomesdev.exchangeapp.data.local.ExchangeDao
import com.dgomesdev.exchangeapp.data.remote.ExchangeService
import com.dgomesdev.exchangeapp.domain.model.ExchangeValues
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val service: ExchangeService,
    private val exchangeDao: ExchangeDao
) : ExchangeRepository {
    override suspend fun getExchangeValues(coins: String) = flow {
            emit(service.getExchangeValues(coins).values.first())
    }

    override suspend fun save(exchangeValues: ExchangeValues) {
        exchangeDao.save(exchangeValues)
    }

    override fun list(): Flow<List<ExchangeValues>> = exchangeDao.getAll()
}