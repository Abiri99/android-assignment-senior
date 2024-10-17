package com.adyen.android.assignment.ui.places

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adyen.android.assignment.ui.commonUi.Colors
import com.adyen.android.assignment.ui.places.uiComponents.PlacesAppBar
import com.adyen.android.assignment.ui.places.uiComponents.PlacesList

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    viewModel: PlacesViewModel,
    requestLocationPermissionCallback: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.observePlacesNearby()
    }

    when (uiState) {
        PlacesUiState.LocationPermissionRequired -> {
            requestLocationPermissionCallback()
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
        is PlacesUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: ${(uiState as PlacesUiState.Error).exception}")
            }
        }
    }
}
