package com.adyen.android.assignment.data.repository

import com.adyen.android.assignment.data.api.PlacesService
import com.adyen.android.assignment.data.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.data.api.model.Place
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlacesRepository(
    private val placesService: PlacesService,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    @Volatile
    private var cachedPlaces: List<Place>? = null

    fun getCachedPlaces(): List<Place> {
        return requireNotNull(cachedPlaces) {
            "Places are not cached. Try to call `fetchPlacesNearby` method first."
        }
    }

    suspend fun fetchPlacesNearby(latitude: Double, longitude: Double): List<Place> =
        withContext(coroutineDispatcher) {
            val query = VenueRecommendationsQueryBuilder()
                .setLatitudeLongitude(latitude, longitude)
                .build()
            val response = placesService.getPlacesNearby(query)
            if (response.isSuccessful) {
                // HTTP 200 code
                val responseBody = response.body()
                val result = responseBody?.results ?: throw EmptyResponseBodyHttpException()
                cachedPlaces = result
                result
            } else {
                // Manage error code 400 bad request
                if (response.code() == 400) {
                    throw BadRequestHttpException()
                }

                // Manage error code 401 unauthorized
                if (response.code() == 401) {
                    throw UnauthorizedHttpException()
                }

                throw HttpException(errorCode = response.code(), response.message())
            }
        }
}

class EmptyResponseBodyHttpException : Exception()
class BadRequestHttpException : Exception()
class UnauthorizedHttpException : Exception()
class HttpException(val errorCode: Int, override val message: String) : Exception()
