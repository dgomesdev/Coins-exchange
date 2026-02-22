package com.dgomesdev.exchangeapp.data.remote

import com.dgomesdev.exchangeapp.domain.ClientErrorException
import com.dgomesdev.exchangeapp.domain.CoinNotFoundException
import com.dgomesdev.exchangeapp.domain.ConversionPair
import com.dgomesdev.exchangeapp.domain.InvalidCoinsException
import com.dgomesdev.exchangeapp.domain.NetworkErrorException
import com.dgomesdev.exchangeapp.domain.ServerErrorException
import com.dgomesdev.exchangeapp.domain.TimeoutException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class ExchangeRemoteDataSourceImplTest {

    @Mock
    private lateinit var mockApi: ExchangeApi
    
    companion object {
        private val mockExchangeValue = ExchangeValue(
            code = "USD",
            codein = "BRL",
            name = "Dólar Americano/Real Brasileiro",
            high = "5.734",
            low = "5.7279",
            varBid = "-0.0054",
            pctChange = "-0.09",
            bid = "5.7276",
            ask = "5.7282",
            timestamp = "1618315045",
            createDate = "2021-04-13 08:57:27"
        )
        private val mockConversionPair = ConversionPair.USDBRL
        private val mockExchangeResponse: ExchangeResponse = mapOf(mockConversionPair.name to mockExchangeValue)
        private const val INVALID_COINS_MESSAGE = "Invalid coins."
        private const val COINS_NOT_FOUND_ERROR_MESSAGE = "Coins Not Found Error: 404 - Coins not found"
        private const val NETWORK_ERROR_MESSAGE = "Error: Network connection failed"
        private const val SERVER_ERROR_MESSAGE = "Server error: 500"
        private const val CLIENT_ERROR_MESSAGE = "Client error: 400"
        private const val TIMEOUT_ERROR_MESSAGE = "Request timed out"
    }

    @Test
    fun `getExchangeValues successful response`() {
        runTest {
            // Given
            mockApi = mock<ExchangeApi> {
                on { getExchangeValues(mockConversionPair.coins) } doReturn mockExchangeResponse
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)

            // When
            val result = remoteDataSource.getExchangeValues(mockConversionPair.coins)

            // Then
            assertEquals(Result.success(mockExchangeResponse), result)
        }
    }

    @Test
    fun `getExchangeValues empty coin string`() {
        runTest {
            // Given
            mockApi = mock<ExchangeApi> {}
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)

            // When
            val result = remoteDataSource.getExchangeValues("")

            // Then
            assertEquals(
                Result.failure(
                    InvalidCoinsException(INVALID_COINS_MESSAGE)
                ),
                result
            )
        }
    }

    @Test
    fun `getExchangeValues invalid coin string`() {
        runTest {
            // Given
            val responseErrorBody = """
        {"status":404,"code":"CoinNotExists","message":"moeda nao encontrada INVALIDCOIN-BRL"}
    """.trimIndent().toResponseBody(null)

            val mockHttpException = HttpException(
                Response.error<Any>(404, responseErrorBody)
            )
            mockApi = mock<ExchangeApi> {
                on {
                    getExchangeValues("invalidcoin")
                } doThrow mockHttpException
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val result = remoteDataSource.getExchangeValues("invalidcoin")
            

            // Then
            assertEquals(
                Result.failure(
                    CoinNotFoundException(COINS_NOT_FOUND_ERROR_MESSAGE)
                ),
                result
            )
        }
    }

    @Test
    fun `getExchangeValues mixed valid and invalid coins`() {
        runTest {
            // Given
            val responseErrorBody = """
        {"status":404,"code":"CoinNotExists","message":"moeda nao encontrada INVALIDCOIN-BRL"}
    """.trimIndent().toResponseBody(null)

            val mockHttpException = HttpException(
                Response.error<Any>(404, responseErrorBody)
            )
            mockApi = mock<ExchangeApi> {
                on {
                    getExchangeValues("${mockConversionPair.coins}, invalidcoin")
                } doThrow mockHttpException
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val result = remoteDataSource.getExchangeValues("${mockConversionPair.coins}, invalidcoin")

            // Then
            assertEquals(
                Result.failure(
                    CoinNotFoundException(COINS_NOT_FOUND_ERROR_MESSAGE)
                ),
                result
            )
        }
    }

    @Test
    fun `getExchangeValues API network error`() {
        runTest {
            // Given
            mockApi = mock<ExchangeApi> {
                on {
                    getExchangeValues(mockConversionPair.coins)
                } doThrow(IOException(NETWORK_ERROR_MESSAGE))
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val result = remoteDataSource.getExchangeValues(mockConversionPair.coins)

            // Then
            assertEquals(
                Result.failure(
                    NetworkErrorException(NETWORK_ERROR_MESSAGE)
                ),
                result
            )
        }
    }

    @Test
    fun `getExchangeValues API server error  5xx `() {
        runTest {
            // Given
            val mockHttpException = HttpException(
                Response.error<Any>(
                    500,
                    SERVER_ERROR_MESSAGE.toResponseBody(null)
                )
            )
            mockApi = mock<ExchangeApi> {
                on {
                    getExchangeValues(mockConversionPair.coins)
                } doThrow mockHttpException
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val result = remoteDataSource.getExchangeValues(mockConversionPair.coins)

            // Then
            assertEquals(
                Result.failure(
                    ServerErrorException(mockHttpException.code(), mockHttpException)
                ),
                result
            )
        }
    }

    @Test
    fun `getExchangeValues API client error  4xx `() {
        runTest {
            // Given
            val mockHttpException = HttpException(
                Response.error<Any>(
                    400,
                    CLIENT_ERROR_MESSAGE.toResponseBody(null)
                )
            )
            mockApi = mock<ExchangeApi> {
                on {
                    getExchangeValues(mockConversionPair.coins)
                } doThrow mockHttpException
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val result = remoteDataSource.getExchangeValues(mockConversionPair.coins)

            // Then
            assertEquals(
                Result.failure(
                    ClientErrorException(mockHttpException.code(), mockHttpException)
                ),
                result
            )
        }
    }

    @Test
    fun `getExchangeValues API call timeout`() {
        runTest {
            // Given
            mockApi = mock<ExchangeApi> {
                on {
                    getExchangeValues(mockConversionPair.coins)
                } doThrow RuntimeException(SocketTimeoutException(TIMEOUT_ERROR_MESSAGE))
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val result = remoteDataSource.getExchangeValues(mockConversionPair.coins)

            // Then
            assertEquals(
                Result.failure(
                    TimeoutException(TIMEOUT_ERROR_MESSAGE)
                ),
                result
            )
        }
    }
}
