package com.dgomesdev.exchangeapp.domain

enum class ConversionPair(val pair: String) {
    USDBRL("USD-BRL"),
    EURBRL("EUR-BRL"),
    BRLUSD("BRL-USD"),
    EURUSD("EUR-USD"),
    BRLEUR("BRL-EUR"),
    USDEUR("USD-EUR");

    companion object {
        fun fromEntity(pairString: String): ConversionPair {
            return entries.find { it.name.equals(pairString, ignoreCase = true) }
                ?: throw IllegalArgumentException(
                    "No Coin enum constant found for conversion pair: $pairString"
                )
        }
    }
}