package com.adyen.android.assignment

import android.app.Application
import com.adyen.android.assignment.data.api.PlacesService
import com.adyen.android.assignment.data.repository.PlacesRepository
import com.adyen.android.assignment.domain.CategorizeAndSortPlacesUseCase

class AssignmentApplication : Application() {

    // Below are the dependencies that are bound to the lifecycle of the App.
    // Note: For such a small app, I felt that manual dependency injection is enough, considering
    // that DI libraries usually have a lot of boilerplate.

    private val placesService = PlacesService.instance

    val categorizeAndSortPlacesUseCase = CategorizeAndSortPlacesUseCase()

    val placesRepository = PlacesRepository(placesService = placesService)
}
