package com.dgomesdev.exchangeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dgomesdev.exchangeapp.domain.model.ExchangeValues

@Database(entities = [ExchangeValues::class], version = 1, exportSchema = false)
abstract class ExchangeDatabase : RoomDatabase() {

    abstract fun exchangeDao(): ExchangeDao
}