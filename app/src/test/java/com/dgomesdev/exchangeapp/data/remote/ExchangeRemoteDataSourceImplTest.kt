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
import kotlin.test.assertFailsWith

@RunWith(MockitoJUnitRunner::class)
class ExchangeRemoteDataSourceImplTest {

    @Mock
    private lateinit var mockApi: ExchangeApi

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

    private val invalidCoinsMessage = "Invalid coins."
    private val coinsNotFoundErrorMessage = "Coins Not Found Error: 404 - Coins not found"
    private val networkErrorMessage = "Error: Network connection failed"
    private val serverErrorMessage = "Server error: 500"
    private val clientErrorMessage = "Client error: 400"
    private val timeoutErrorMessage = "Request timed out"

    @Test
    fun `getExchangeValues successful response`() {
        runTest {
            // Given
            mockApi = mock<ExchangeApi> {
                onBlocking { getExchangeValues(mockConversionPair.coins) } doReturn mockExchangeResponse
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val result = remoteDataSource.getExchangeValues(mockConversionPair.coins)

            // Then
            assertEquals(mockExchangeResponse, result)
        }
    }

    @Test
    fun `getExchangeValues empty coin string`() {
        runTest {
            // Given
            mockApi = mock<ExchangeApi> {}
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)

            // When
            val exception = assertFailsWith<InvalidCoinsException> {
                remoteDataSource.getExchangeValues("")
            }

            // Then
            assertEquals(invalidCoinsMessage, exception.message)
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
                onBlocking {
                    getExchangeValues("invalidcoin")
                } doThrow mockHttpException
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val exception = assertFailsWith<CoinNotFoundException> {
                remoteDataSource.getExchangeValues("invalidcoin")
            }

            // Then
            assertEquals(coinsNotFoundErrorMessage, exception.message)
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
                onBlocking {
                    getExchangeValues("${mockConversionPair.coins}, invalidcoin")
                } doThrow mockHttpException
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val exception = assertFailsWith<CoinNotFoundException> {
                remoteDataSource.getExchangeValues("${mockConversionPair.coins}, invalidcoin")
            }

            // Then
            assertEquals(coinsNotFoundErrorMessage, exception.message)
        }
    }

    @Test
    fun `getExchangeValues API network error`() {
        runTest {
            // Given
            mockApi = mock<ExchangeApi> {
                onBlocking {
                    getExchangeValues(mockConversionPair.coins)
                } doThrow(RuntimeException(IOException(networkErrorMessage)))
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val exception = assertFailsWith<NetworkErrorException> {
                remoteDataSource.getExchangeValues(mockConversionPair.coins)
            }

            // Then
            assertEquals(networkErrorMessage, exception.message)
        }
    }

    @Test
    fun `getExchangeValues API server error  5xx `() {
        runTest {
            // Given
            val mockHttpException = HttpException(
                Response.error<Any>(
                    500,
                    serverErrorMessage.toResponseBody(null)
                )
            )
            mockApi = mock<ExchangeApi> {
                onBlocking {
                    getExchangeValues(mockConversionPair.coins)
                } doThrow mockHttpException
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val exception = assertFailsWith<ServerErrorException> {
                remoteDataSource.getExchangeValues(mockConversionPair.coins)
            }

            // Then
            assertEquals(serverErrorMessage, exception.message)
        }
    }

    @Test
    fun `getExchangeValues API client error  4xx `() {
        runTest {
            // Given
            val mockHttpException = HttpException(
                Response.error<Any>(
                    400,
                    clientErrorMessage.toResponseBody(null)
                )
            )
            mockApi = mock<ExchangeApi> {
                onBlocking {
                    getExchangeValues(mockConversionPair.coins)
                } doThrow mockHttpException
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val exception = assertFailsWith<ClientErrorException> {
                remoteDataSource.getExchangeValues(mockConversionPair.coins)
            }

            // Then
            assertEquals(clientErrorMessage, exception.message)
        }
    }

    @Test
    fun `getExchangeValues API call timeout`() {
        runTest {
            // Given
            mockApi = mock<ExchangeApi> {
                onBlocking {
                    getExchangeValues(mockConversionPair.coins)
                } doThrow RuntimeException(SocketTimeoutException(timeoutErrorMessage))
            }
            val remoteDataSource = ExchangeRemoteDataSourceImpl(mockApi)


            // When
            val exception = assertFailsWith<TimeoutException> {
                remoteDataSource.getExchangeValues(mockConversionPair.coins)
            }

            // Then
            assertEquals(timeoutErrorMessage, exception.message)
        }
    }
}