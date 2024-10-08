package com.adyen.android.assignment.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adyen.android.assignment.data.repository.PlacesRepository

/**
 * This factory helps us letting the ViewModelProvider know how an instance of this ViewModel
 * should be created with it's dependencies.
 */
class SplashViewModelFactory(
    private val placesRepository: PlacesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel(placesRepository = placesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
