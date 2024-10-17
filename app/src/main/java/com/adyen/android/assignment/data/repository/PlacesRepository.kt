package com.adyen.android.assignment.data.repository

import com.adyen.android.assignment.data.api.PlacesService
import com.adyen.android.assignment.data.api.VenueRecommendationsQueryBuilder
import com.adyen.android.assignment.data.api.model.PlacesResponse
import com.adyen.android.assignment.data.service.LocationService
import com.adyen.android.assignment.data.service.NetworkService
import com.adyen.android.assignment.di.IOCoroutineDispatcher
import com.adyen.android.assignment.domain.NoNetworkAvailableException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val locationService: LocationService,
    private val networkService: NetworkService,
    private val placesService: PlacesService,
    private val retrofitApiHandler: RetrofitApiHandler,
    @IOCoroutineDispatcher private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun observePlacesNearby(): Flow<Result<PlacesResponse>> = locationService
        .getUserLocation()
        .map { locationResult ->
            if (!networkService.isAvailable()) {
                Result.failure(NoNetworkAvailableException)
            } else {
                locationResult.fold(
                    onSuccess = { locationPair ->
                        val query = VenueRecommendationsQueryBuilder()
                            .setLatitudeLongitude(locationPair.first, locationPair.second)
                            .build()

                        retrofitApiHandler.getData(placesService.getPlacesNearby(query))
                    },
                    onFailure = { exception ->
                        Result.failure(exception)
                    }
                )
            }
        }.flowOn(coroutineDispatcher)
}
