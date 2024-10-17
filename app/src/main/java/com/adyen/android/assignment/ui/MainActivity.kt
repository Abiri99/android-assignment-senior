package com.adyen.android.assignment.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adyen.android.assignment.data.repository.PlacesRepository
import com.adyen.android.assignment.data.service.LocationService
import com.adyen.android.assignment.domain.CategorizeAndSortPlacesUseCase
import com.adyen.android.assignment.ui.places.PlacesScreen
import com.adyen.android.assignment.ui.places.PlacesViewModel
import com.adyen.android.assignment.ui.splash.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    @Inject
    private lateinit var locationService: LocationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Window decoration (For the purpose of better UI)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false // Set to 'true' for dark icons
        insetsController.isAppearanceLightNavigationBars = false // Set to 'true' for dark icons

        // End Window decoration

        setContent {
            val navController = rememberNavController()

            val screenModifier = Modifier.fillMaxSize()

            // Note: When the app gets bigger, it's better to hold the screen keys in a variable to prevent typos.
            NavHost(navController, startDestination = "splash") {
                composable("splash") { backStackEntry ->
                    SplashScreen(
                        modifier = screenModifier,
                        navigateToPlacesScreenCallback = {
                            navController.navigate("places") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    )
                }

                composable("places") { backStackEntry ->
                    val placesViewModel = hiltViewModel<PlacesViewModel>()
                    PlacesScreen(
                        modifier = screenModifier,
                        viewModel = placesViewModel,
                        requestLocationPermissionCallback = {
                            requestLocationPermission()
                        }
                    )
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission is granted. Continue the action or workflow
                // in your app.
            } else {
                // Explain to the user that the feature is unavailable because
                // the feature requires a permission that the user has denied.
                // At the same time, respect the user's decision. Don't link to
                // system settings in an effort to convince the user to change
                // their decision.
            }
            return
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE,
        )
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
