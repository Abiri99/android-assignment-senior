package com.adyen.android.assignment.domain

import com.adyen.android.assignment.data.api.model.Place

// Since this UseCase is only used by the PlacesViewModel, it could be a method inside the PlacesViewModel as well.
class CategorizeAndSortPlacesUseCase {
    fun execute(places: List<Place>): Map<String, List<Place>> {
        val result = mutableMapOf<String, MutableList<Place>>()

        places.forEach { place ->
            place.categories.forEach { placeCategory ->
                val currentPlacesInCategory = result.getOrPut(placeCategory.name) { mutableListOf() }

                val insertionIndex = currentPlacesInCategory.binarySearchBy(place.distance) { it.distance }
                if (insertionIndex < 0) {
                    currentPlacesInCategory.add(-insertionIndex - 1, place)
                } else {
                    currentPlacesInCategory.add(insertionIndex, place)
                }
            }
        }

        return result
    }
}
