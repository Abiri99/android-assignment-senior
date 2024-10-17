package com.adyen.android.assignment.data.service

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class LocationAccessDeniedException : Exception()

@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fun getUserLocation(): Flow<Result<Pair<Double, Double>>> = flow {
        // Check if user has location permission
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If permission is not granted, you should request the necessary permissions from the user
            emit(Result.failure(LocationAccessDeniedException()))
            return@flow
        }

        // Get the last known location
        val locationResult: Task<Location> = fusedLocationClient.lastLocation
        val location = locationResult.await()

        // If location is available, emit success
        emit(Result.success(Pair(location.latitude, location.longitude)))

        // Whenever coarse location changes, emit a new result (using callbackFlow)
        emitAll(getCoarseLocationUpdates())
    }

    @SuppressLint("MissingPermission")
    private fun getCoarseLocationUpdates(): Flow<Result<Pair<Double, Double>>> = callbackFlow {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = android.location.LocationListener { location ->
            val lat = location.latitude
            val long = location.longitude
            trySend(Result.success(Pair(lat, long)))
        }

        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000L, // Minimum time interval between updates (milliseconds)
            10f, // Minimum distance between updates (meters)
            locationListener
        )

        awaitClose {
            locationManager.removeUpdates(locationListener)
        }
    }
}
