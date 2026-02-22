package com.dgomesdev.exchangeapp.domain

open class ApiException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class InvalidCoinsException(message: String = "Invalid coins.", cause: Throwable? = null) : ApiException(message, cause)
class CoinNotFoundException(message: String = "Coin not found.", cause: Throwable? = null) : ApiException(message, cause)
class NetworkErrorException(message: String = "A network error occurred.", cause: Throwable? = null) : ApiException(message, cause)
class ServerErrorException(httpStatusCode: Int, cause: Throwable? = null) : ApiException("Server error: $httpStatusCode", cause)
class ClientErrorException(httpStatusCode: Int, cause: Throwable? = null) : ApiException("Client error: $httpStatusCode", cause)
class TimeoutException(message: String = "The request timed out.", cause: Throwable? = null) : ApiException(message, cause)
class UnexpectedApiException(message: String = "An unexpected API error occurred.", cause: Throwable? = null) : ApiException(message, cause)
class RepositoryError(message: String = "An unexpected repository error occurred.", cause: Throwable? = null) : ApiException(message, cause)