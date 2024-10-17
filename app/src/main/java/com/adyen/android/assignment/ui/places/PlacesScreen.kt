package com.adyen.android.assignment.ui.places

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adyen.android.assignment.ui.commonUi.Colors
import com.adyen.android.assignment.ui.commonUi.TAG
import com.adyen.android.assignment.ui.places.uiComponents.PlacesAppBar
import com.adyen.android.assignment.ui.places.uiComponents.PlacesList
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    viewModel: PlacesViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is PlacesUiState.Success -> {
            if ((uiState as PlacesUiState.Success).items.isNotEmpty()) {
                Column(
                    modifier,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    PlacesAppBar()
                    PlacesList(
                        modifier = Modifier.fillMaxSize(),
                        data = (uiState as PlacesUiState.Success).items
                    )
                }
            } else {
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No places were found! ☹️",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        PlacesUiState.Loading -> {
            // nothing to render
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator(color = Colors.adyenGreenColor)
            }
        }
        PlacesUiState.LocationPermissionRequired -> {
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = { granted ->
                    if (granted) {
                        onGranted()
                    } else {
                        if (showRationale) {
                            // Already shown a rationale to the user but they haven't accepted
                            requestPermission = false
                            onDegradeFunctionality()
                        } else {
                            if (permissionState.status.shouldShowRationale) {
                                showRationale = true
                            } else {
                                // degrade the app
                                onDegradeFunctionality()
                            }
                        }
                    }
                }
            )
        }
    }
}
