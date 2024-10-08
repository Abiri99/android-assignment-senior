package com.adyen.android.assignment.ui.places

import com.adyen.android.assignment.data.api.model.Place

sealed class PlacesUiState {
    data object Initial : PlacesUiState()
    class ItemsCategorizedAndSorted(val items: Map<String, List<Place>>) : PlacesUiState()
}
