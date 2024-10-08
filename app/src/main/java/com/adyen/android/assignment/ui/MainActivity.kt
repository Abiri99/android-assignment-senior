package com.adyen.android.assignment.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adyen.android.assignment.AssignmentApplication
import com.adyen.android.assignment.data.repository.PlacesRepository
import com.adyen.android.assignment.ui.places.PlacesScreen
import com.adyen.android.assignment.ui.splash.SplashScreen
import com.adyen.android.assignment.ui.splash.SplashViewModel
import com.adyen.android.assignment.ui.splash.SplashViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var placesRepository: PlacesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Creating dependencies

        placesRepository = (application as AssignmentApplication).placesRepository

        // End creating dependencies

        setContent {
            val navController = rememberNavController()

            val screenModifier = Modifier
                .fillMaxSize()
                .safeContentPadding() // TODO: make sure that this is the right api to use

            NavHost(navController, startDestination = "splash") {
                composable("splash") {
                    val splashViewModelFactory = remember {
                        SplashViewModelFactory(placesRepository)
                    }

                    // viewmodel lifecycle bounds to the scope of SplashScreen
                    val splashViewModel: SplashViewModel = viewModel(factory = splashViewModelFactory)

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

                composable("places") {
                    PlacesScreen(
                        modifier = screenModifier,
                        getCachedPlacesCallback = { placesRepository.getCachedPlaces() }
                    )
                }
            }
        }
    }
}
