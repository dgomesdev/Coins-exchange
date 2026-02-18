package com.dgomesdev.exchangeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExchangeDao {

    @Query("SELECT * FROM coin_table")
    suspend fun getAllExchangeValues(): List<ExchangeLocalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveExchange(exchangeValues: ExchangeLocalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTimestamp(timestamp: LastUpdatedTimestamp)

    @Query("SELECT * FROM last_updated_timestamp WHERE id = 0")
    suspend fun getLastUpdatedTimestamp(): LastUpdatedTimestamp?
}