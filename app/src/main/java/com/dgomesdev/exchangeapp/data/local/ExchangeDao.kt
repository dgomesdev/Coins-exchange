package com.dgomesdev.exchangeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeDao {

    @Query("SELECT * FROM coin_table")
    fun getAll(): Flow<List<ExchangeLocalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(exchangeValues: ExchangeLocalEntity)
}