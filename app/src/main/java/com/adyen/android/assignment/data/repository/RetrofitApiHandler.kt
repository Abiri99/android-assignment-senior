package com.adyen.android.assignment.data.repository

import com.adyen.android.assignment.domain.ExceptionHandler
import com.adyen.android.assignment.domain.FatalException
import com.adyen.android.assignment.domain.HttpException
import retrofit2.Response
import javax.inject.Inject

class RetrofitApiHandler @Inject constructor(
    private val exceptionHandler: ExceptionHandler
) {
    fun <D> getData(response: Response<D>): Result<D> {
        return if (response.isSuccessful) {
            val data = response.body()
            if (data != null) {
                Result.success(data)
            } else {
                Result.failure(HttpException.EmptyResponseBodyException)
            }
        } else {
            val errorCode = response.code()
            val httpException = exceptionHandler.getHttpExceptionOrNull(errorCode)

            if (httpException != null) {
                Result.failure(httpException)
            } else {
                Result.failure(
                    FatalException(
                        cause = null,
                        message = """
                            header: ${response.headers()}
                            body: ${response.errorBody()}
                            errorCode: ${response.code()}
                            errorBody: ${response.errorBody()}
                        """.trimIndent()
                    )
                )
            }
        }
    }
}
