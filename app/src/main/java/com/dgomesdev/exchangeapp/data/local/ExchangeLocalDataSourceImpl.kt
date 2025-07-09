package com.dgomesdev.exchangeapp.data.local

import kotlinx.coroutines.flow.Flow

class ExchangeLocalDataSourceImpl(val exchangeDao: ExchangeDao) : ExchangeLocalDataSource {
    override fun getAll(): Flow<List<ExchangeLocalEntity>> = exchangeDao.getAll()


    override suspend fun save(exchangeValues: ExchangeLocalEntity) {
        exchangeDao.save(exchangeValues)
    }
}