package com.adyen.android.assignment.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.data.repository.BadRequestHttpException
import com.adyen.android.assignment.data.repository.PlacesRepository
import com.adyen.android.assignment.data.repository.UnauthorizedHttpException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState

    fun fetchPlacesNearby(lat: Double, long: Double) {
        viewModelScope.launch {
            fetchPlacesNearbyWithRetry(lat, long)
        }
    }

    fun showError(message: String) {
        _uiState.value = SplashUiState.Error(message)
    }

    fun resetState() {
        _uiState.value = SplashUiState.Loading
    }

    private suspend fun fetchPlacesNearbyWithRetry(lat: Double, long: Double) {
        val maxRetries = 5
        var retryCount = 0
        var delayTime = 1000L // Start with 1 second

        while (retryCount < maxRetries) {
            try {
                placesRepository.fetchPlacesNearby(lat, long)
                _uiState.value = SplashUiState.Success
                break
            } catch (e: Exception) {
                if (e is UnauthorizedHttpException) {
                    _uiState.value = SplashUiState.Error("Please log in again.")
                    break
                }

                if (e is BadRequestHttpException) {
                    Log.d(TAG, "The request is not correct.")
                    _uiState.value = SplashUiState.Error("Please update the app.")
                    break
                }

                retryCount++
                if (retryCount >= maxRetries) {
                    Log.d(TAG, e.message ?: "Unknown issue")
                    _uiState.value = SplashUiState.Error("Unknown issue, please contact the support.") // Before going to production, we can cover more http error codes with better messages.
                } else {
                    delay(delayTime)
                    delayTime *= 2 // Exponential backoff strategy
                }
            }
        }
    }

    companion object {
        private const val TAG = "SplashViewModel"
    }
}
