package com.adyen.android.assignment.ui.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adyen.android.assignment.data.repository.PlacesRepository
import com.adyen.android.assignment.data.service.LocationAccessDeniedException
import com.adyen.android.assignment.domain.CategorizeAndSortPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val categorizeAndSortPlacesUseCase: CategorizeAndSortPlacesUseCase,
    private val placesRepository: PlacesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<PlacesUiState>(PlacesUiState.Loading)
    val uiState: StateFlow<PlacesUiState> = _uiState

    fun observePlacesNearby() {
        viewModelScope.launch {
            _uiState.value = PlacesUiState.Loading
            placesRepository.observePlacesNearby()
                .collectLatest { placesResult ->
                    placesResult.fold(
                        onSuccess = { placesResponse ->
                            val data = categorizeAndSortPlacesUseCase.execute(placesResponse.results)
                            _uiState.value = PlacesUiState.Success(items = data)
                        },
                        onFailure = { throwable ->
                            when (throwable) {
                                is LocationAccessDeniedException -> {
                                    _uiState.value = PlacesUiState.LocationPermissionRequired
                                }
                                // TODO: Cover other exceptions
                            }
                        }
                    )
                }
        }
    }

    fun degradeContent() {
        // Show degraded content when user hasn't granted access to permission
    }
}
