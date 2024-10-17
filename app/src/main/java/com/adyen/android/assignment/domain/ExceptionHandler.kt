package com.adyen.android.assignment.domain

// Exceptions that we cannot recover from and we should let the app crash when happened
data class FatalException(
    override val cause: Throwable?,
    override val message: String?
): Exception()

data object NoNetworkAvailableException: Exception()

sealed class HttpException: Exception() {
    data object EmptyResponseBodyException: HttpException()
    data object UnAuthorizedException: HttpException()
    // TODO: cover more http errors
}

class ExceptionHandler {
    fun getHttpExceptionOrNull(code: Int): HttpException? {
        return when(code) {
            403 -> HttpException.UnAuthorizedException
            else -> null
        }
    }
}