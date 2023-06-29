package com.dgomesdev.exchangeapp.data.remote.di

import com.dgomesdev.exchangeapp.data.remote.ExchangeService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit =
        Retrofit.Builder()
        .baseUrl("https://economia.awesomeapi.com.br")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideService(retrofit: Retrofit): ExchangeService =
        retrofit.create(ExchangeService::class.java)
}