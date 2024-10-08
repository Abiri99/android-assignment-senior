package com.adyen.android.assignment.ui.splash

sealed class SplashState {
    data object Loading : SplashState()
    data object Success : SplashState()
    data class Error(val message: String) : SplashState()
}
