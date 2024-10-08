package com.adyen.android.assignment.ui.places

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.adyen.android.assignment.ui.places.uiComponents.PlacesAppBar
import com.adyen.android.assignment.ui.places.uiComponents.PlacesList

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    viewModel: PlacesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.categoriseAndSortPlaces()
    }

    when (uiState) {
        is PlacesUiState.ItemsCategorizedAndSorted -> {
            if ((uiState as PlacesUiState.ItemsCategorizedAndSorted).items.isNotEmpty()) {
                Column(
                    modifier,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    PlacesAppBar()
                    PlacesList(
                        modifier = Modifier.fillMaxSize(),
                        items = (uiState as PlacesUiState.ItemsCategorizedAndSorted).items
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
        PlacesUiState.Initial -> {
            // nothing to render
        }
    }
}
