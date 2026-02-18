package com.dgomesdev.exchangeapp.data.remote

import android.util.Log
import com.dgomesdev.exchangeapp.domain.ClientErrorException
import com.dgomesdev.exchangeapp.domain.CoinNotFoundException
import com.dgomesdev.exchangeapp.domain.InvalidCoinsException
import com.dgomesdev.exchangeapp.domain.NetworkErrorException
import com.dgomesdev.exchangeapp.domain.ServerErrorException
import com.dgomesdev.exchangeapp.domain.TimeoutException
import com.dgomesdev.exchangeapp.domain.UnexpectedApiException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class ExchangeRemoteDataSourceImpl @Inject constructor(
    private val api: ExchangeApi
) : ExchangeRemoteDataSource {
    override suspend fun getExchangeValues(coins: String): Result<ExchangeResponse> {
        Log.i("ExchangeRemoteDataSourceImpl", "getExchangeValues: $coins")
        return runCatching {
            try {
                if (coins.isBlank()) {
                    throw RuntimeException("Invalid coins.")
                }
                val result = api.getExchangeValues(coins)
                Log.i("ExchangeRemoteDataSourceImpl", "result: $result")
                result
            } catch (e: HttpException) {
                when (val httpCode = e.code()) {
                    404 -> {
                        throw CoinNotFoundException(
                            message = "Coins Not Found Error: $httpCode - Coins not found",
                            cause = e
                        )
                    }
                    in 400..499 -> {
                        throw ClientErrorException(
                            httpStatusCode = httpCode,
                            cause = e
                        )
                    }
                    in 500..599 -> {
                        throw ServerErrorException(
                            httpStatusCode = httpCode,
                            cause = e
                        )
                    }
                    else -> {
                        throw UnexpectedApiException("Unexpected HTTP error: $httpCode", e)
                    }
                }
            } catch (e: SocketTimeoutException) {
                throw TimeoutException(cause = e)
            } catch (e: IOException) {
                throw NetworkErrorException(cause = e)
            } catch (e: RuntimeException) {
                if (e.message == "Invalid coins.") {
                    throw InvalidCoinsException(message = e.message ?: "Invalid coins.", cause = e)
                }
                if (e.cause is SocketTimeoutException) {
                    throw TimeoutException(message = "${e.cause?.message}", cause = e.cause)
                }
                if (e.cause is IOException) {
                    throw NetworkErrorException(message = "${e.cause?.message}", cause = e.cause)
                }
                throw UnexpectedApiException("An unexpected runtime error occurred", e)
            } catch (e: Exception) {
                throw UnexpectedApiException("An unexpected error occurred", e)
            }
        }
    }
}