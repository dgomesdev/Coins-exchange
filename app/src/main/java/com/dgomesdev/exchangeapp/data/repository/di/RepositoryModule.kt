package com.dgomesdev.exchangeapp.data.repository.di

import com.dgomesdev.exchangeapp.data.repository.ExchangeRepository
import com.dgomesdev.exchangeapp.data.repository.ExchangeRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(exchangeRepositoryImpl: ExchangeRepositoryImpl): ExchangeRepository
}