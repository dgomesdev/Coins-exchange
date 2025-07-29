package com.dgomesdev.exchangeapp.domain

import com.dgomesdev.exchangeapp.data.local.ExchangeLocalEntity
import com.dgomesdev.exchangeapp.data.remote.ExchangeRemoteEntity

data class ExchangeModel(
    val conversionPair: ConversionPair,
    val bid: Double,
    val code: String
) {

    fun toLocalEntity() = ExchangeLocalEntity(
        this.conversionPair.name,
        this.bid,
        this.code
    )

    companion object {
        fun fromRemoteEntity(entity: ExchangeRemoteEntity): ExchangeModel =
             ExchangeModel(
                 ConversionPair.fromEntity(entity.key),
                entity.value.bid.toDoubleOrNull() ?: 0.0,
                 entity.value.code
            )

        fun fromLocalEntity(entity: ExchangeLocalEntity): ExchangeModel =
            ExchangeModel(
                ConversionPair.fromEntity(entity.conversionPair),
                entity.bid,
                entity.code
            )
    }
}