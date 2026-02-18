package com.dgomesdev.exchangeapp.domain

open class ApiException(message: String, cause: Throwable? = null) : Exception(message, cause)

class InvalidCoinsException(message: String = "Invalid coins.", cause: Throwable? = null) : ApiException(message, cause)
class CoinNotFoundException(message: String = "Coin not found.", cause: Throwable? = null) : ApiException(message, cause)
class NetworkErrorException(message: String = "A network error occurred.", cause: Throwable? = null) : ApiException(message, cause)
class ServerErrorException(httpStatusCode: Int, cause: Throwable? = null) : ApiException("Server error: $httpStatusCode", cause)
class ClientErrorException(httpStatusCode: Int, cause: Throwable? = null) : ApiException("Client error: $httpStatusCode", cause)
class TimeoutException(message: String = "The request timed out.", cause: Throwable? = null) : ApiException(message, cause)
class UnexpectedApiException(message: String = "An unexpected API error occurred.", cause: Throwable? = null) : ApiException(message, cause)
class RepositoryError(message: String = "An unexpected repository error occurred.", cause: Throwable? = null) : ApiException(message, cause)