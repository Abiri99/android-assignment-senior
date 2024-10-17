package com.adyen.android.assignment.ui.places

import com.adyen.android.assignment.data.api.model.Place

sealed class PlacesUiState {
    data object Loading : PlacesUiState()
    data object LocationPermissionRequired: PlacesUiState()
    data class Success(val items: Map<String, List<Place>>) : PlacesUiState()
    data class Error(val exception: Exception): PlacesUiState()
}
