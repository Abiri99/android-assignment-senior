package com.adyen.android.assignment

import android.app.Application
import com.adyen.android.assignment.data.api.PlacesService
import com.adyen.android.assignment.data.repository.PlacesRepository
import com.adyen.android.assignment.domain.CategorizeAndSortPlacesUseCase

class AssignmentApplication : Application() {

    // Getting an instance of PlacesService here with the assumption that the
    // lifecycle of this object is in the scope of application.
    private val placesService = PlacesService.instance

    val categorizeAndSortPlacesUseCase = CategorizeAndSortPlacesUseCase()

    val placesRepository = PlacesRepository(placesService = placesService)
}
