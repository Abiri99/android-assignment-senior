package com.adyen.android.assignment.ui.commonUi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.adyen.android.assignment.ui.util.ConnectivityUtils

@Composable
fun InternetAvailabilityChecker(
    onAvailable: () -> Unit,
    onUnavailable: (String) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val isNetworkAvailable = ConnectivityUtils.isInternetAvailable(context)
        if (!isNetworkAvailable) {
            val isAirplaneModeOn = ConnectivityUtils.isAirplaneModeOn(context)
            val message = if (isAirplaneModeOn) {
                "Airplane mode is enabled. Please disable it to access the internet."
            } else {
                "No internet connection. Please check your network settings."
            }
            onUnavailable(message)
        } else {
            onAvailable()
        }
    }
}
