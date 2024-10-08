package com.adyen.android.assignment.ui.places

import androidx.lifecycle.ViewModel
import com.adyen.android.assignment.data.repository.PlacesRepository
import com.adyen.android.assignment.domain.CategorizeAndSortPlacesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlacesViewModel(
    private val categorizeAndSortPlacesUseCase: CategorizeAndSortPlacesUseCase,
    private val placesRepository: PlacesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlacesUiState>(PlacesUiState.Initial)
    val uiState: StateFlow<PlacesUiState> = _uiState

    fun categoriseAndSortPlaces() {
        if (uiState.value is PlacesUiState.Initial) {
            val places = placesRepository.getCachedPlaces()
            val categorizedAndSorted = categorizeAndSortPlacesUseCase.execute(places)

            _uiState.value = PlacesUiState.ItemsCategorizedAndSorted(
                items = categorizedAndSorted
            )
        }
    }
}
