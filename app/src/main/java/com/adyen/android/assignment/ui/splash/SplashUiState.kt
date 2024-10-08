package com.adyen.android.assignment.ui.splash

sealed class SplashUiState {
    data object Loading : SplashUiState()
    data object Success : SplashUiState()
    data class Error(val message: String) : SplashUiState()
}
