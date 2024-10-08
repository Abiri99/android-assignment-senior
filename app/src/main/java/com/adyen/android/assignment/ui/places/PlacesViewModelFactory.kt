package com.adyen.android.assignment.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adyen.android.assignment.data.repository.PlacesRepository
import com.adyen.android.assignment.domain.CategorizeAndSortPlacesUseCase

class PlacesViewModelFactory(
    private val categorizeAndSortPlacesUseCase: CategorizeAndSortPlacesUseCase,
    private val placesRepository: PlacesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlacesViewModel(
                categorizeAndSortPlacesUseCase = categorizeAndSortPlacesUseCase,
                placesRepository = placesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
