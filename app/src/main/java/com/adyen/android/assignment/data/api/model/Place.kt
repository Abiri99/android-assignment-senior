package com.adyen.android.assignment.data.api.model

data class Place(
    val fsq_id: String,
    val categories: List<Category>,
    val distance: Int,
    val geocode: GeoCode,
    val location: Location,
    val name: String,
    val timezone: String
)
