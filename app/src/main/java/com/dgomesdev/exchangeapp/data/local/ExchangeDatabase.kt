package com.dgomesdev.exchangeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExchangeLocalEntity::class], version = 1, exportSchema = false)
abstract class ExchangeDatabase : RoomDatabase() {

    abstract fun exchangeDao(): ExchangeDao
}