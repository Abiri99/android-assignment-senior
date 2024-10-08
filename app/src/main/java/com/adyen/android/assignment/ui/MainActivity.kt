package com.adyen.android.assignment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adyen.android.assignment.AssignmentApplication
import com.adyen.android.assignment.data.repository.PlacesRepository
import com.adyen.android.assignment.domain.CategorizeAndSortPlacesUseCase
import com.adyen.android.assignment.ui.places.PlacesScreen
import com.adyen.android.assignment.ui.places.PlacesViewModel
import com.adyen.android.assignment.ui.places.PlacesViewModelFactory
import com.adyen.android.assignment.ui.splash.SplashScreen
import com.adyen.android.assignment.ui.splash.SplashViewModel
import com.adyen.android.assignment.ui.splash.SplashViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var categorizeAndSortPlacesUseCase: CategorizeAndSortPlacesUseCase
    private lateinit var placesRepository: PlacesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creating dependencies

        categorizeAndSortPlacesUseCase = (application as AssignmentApplication).categorizeAndSortPlacesUseCase

        placesRepository = (application as AssignmentApplication).placesRepository

        // End creating dependencies

        // Window decoration

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false // Set to 'true' for dark icons
        insetsController.isAppearanceLightNavigationBars = false // Set to 'true' for dark icons

        // End Window decoration

        setContent {
            val navController = rememberNavController()

            val screenModifier = Modifier.fillMaxSize()

            // TODO: When the app gets bigger, it's better to hold the screen keys in a variable to prevent typos.
            NavHost(navController, startDestination = "splash") {
                composable("splash") { backStackEntry ->
                    val splashViewModelFactory = remember {
                        SplashViewModelFactory(placesRepository)
                    }

                    // viewmodel lifecycle bounds to the scope of SplashScreen
                    val splashViewModel: SplashViewModel = viewModel(
                        viewModelStoreOwner = backStackEntry,
                        factory = splashViewModelFactory
                    )

                    SplashScreen(
                        modifier = screenModifier,
                        viewModel = splashViewModel,
                        navigateToPlacesScreenCallback = {
                            navController.navigate("places") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    )
                }

                composable("places") { backStackEntry ->
                    val placesViewModelFactory = remember {
                        PlacesViewModelFactory(
                            categorizeAndSortPlacesUseCase,
                            placesRepository
                        )
                    }

                    val placesViewModel: PlacesViewModel = viewModel(
                        viewModelStoreOwner = backStackEntry,
                        factory = placesViewModelFactory
                    )

                    PlacesScreen(
                        modifier = screenModifier,
                        viewModel = placesViewModel
                    )
                }
            }
        }
    }
}
