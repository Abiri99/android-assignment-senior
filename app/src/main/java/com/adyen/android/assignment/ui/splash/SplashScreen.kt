package com.adyen.android.assignment.ui.splash

import android.Manifest
import android.app.Activity
import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adyen.android.assignment.ui.common_ui.Colors
import com.adyen.android.assignment.ui.common_ui.InternetAvailabilityChecker
import com.adyen.android.assignment.ui.common_ui.PermissionHandler
import com.google.android.gms.location.LocationServices

private const val TAG = "SplashScreen"

// Composables

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel,
    navigateToPlacesScreenCallback: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // In case we couldn't fetch the location of the user, let's show places nearby the center of Amsterdam.
    // A better approach could be to show places based on user's ip address or his/her last location.
    val amsterdamLat = 52.3676
    val amsterdamLong = 4.9041

    LaunchedEffect(uiState) {
        if (uiState is SplashState.Success) {
            navigateToPlacesScreenCallback()
        }
    }

    InternetAvailabilityChecker(
        onAvailable = {
            // let the app proceed
        },
        onUnavailable = { message ->
            viewModel.showError(message)
        }
    )

    PermissionHandler(
        permission = Manifest.permission.ACCESS_COARSE_LOCATION,
        onGranted = {
            fetchLocation(context) { lat, long ->
                viewModel.fetchPlacesNearby(
                    lat = requireNotNull(lat) { "Failed to get users location despite having permission." },
                    long = requireNotNull(long) { "Failed to get users location despite having permission." },
                )
            }
        },
        onDegradeFunctionality = {
            viewModel.fetchPlacesNearby(amsterdamLat, amsterdamLong)
        },
    )

    SplashScreenContent(
        modifier = modifier,
        showLoadingIndicator = uiState == SplashState.Loading,
    )

    if (uiState is SplashState.Error) {
        val message = (uiState as SplashState.Error).message
        ErrorDialog(
            message = message,
            onRetry = {
                viewModel.resetState()
            },
            onExit = {
                // Handle exit action
                (context as? Activity)?.finish()
            }
        )
    }
}

@Composable
private fun SplashScreenContent(
    modifier: Modifier = Modifier,
    showLoadingIndicator: Boolean,
) {
    Box(
        modifier = modifier.background(Colors.adyenGreenColor),
    ) {
        Column(
            modifier = Modifier.align(BiasAlignment(0.0f, 0.0f)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "🌎",
                fontSize = 42.sp,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Places",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 48.sp,
                color = Color.White,
            )
        }

        if (showLoadingIndicator) {
            LinearProgressIndicator(
                modifier = Modifier.align(BiasAlignment(0.0f, 0.9f,)),
                color = Color.White,
            )
        }
    }
}

@Composable
private fun ErrorDialog(
    message: String,
    onRetry: () -> Unit,
    onExit: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onRetry, // We can discuss this behaviour with UX team
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text("Retry")
            }
        },
        dismissButton = {
            TextButton(onClick = onExit) {
                Text("Exit")
            }
        },
        title = { Text("Error") },
        text = { Text(message) }
    )
}

// End Composables

// Private methods

private fun fetchLocation(context: Context, onLocationFetched: (Double?, Double?) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    try {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onLocationFetched(location.latitude, location.longitude)
                } else {
                    Log.d(TAG, "Obtained location is null.")

                    Toast.makeText(
                        context,
                        "Couldn't find your location. Going to show places near Amsterdam.",
                        Toast.LENGTH_LONG
                    ).show()

                    onLocationFetched(null, null)
                }
            }
    } catch (e: SecurityException) {
        Log.d(TAG, e.message ?: "Security exception occurred")
        Toast.makeText(
            context,
            "Location permission is required to show places near you. Going to show places near Amsterdam.",
            Toast.LENGTH_LONG
        ).show()

        onLocationFetched(null, null)
    }
}

// End Private methods

// Preview

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreenContent(modifier = Modifier.fillMaxSize(), showLoadingIndicator = true)
}

// End Preview