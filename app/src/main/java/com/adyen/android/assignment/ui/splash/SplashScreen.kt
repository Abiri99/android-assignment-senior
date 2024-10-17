package com.adyen.android.assignment.ui.splash

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adyen.android.assignment.ui.commonUi.Colors
import kotlinx.coroutines.delay

private const val TAG = "SplashScreen"

// Composables

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navigateToPlacesScreenCallback: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1000)
        navigateToPlacesScreenCallback()
    }

    Box(
        modifier = modifier.background(Colors.adyenGreenColor)
    ) {
        Column(
            modifier = Modifier.align(BiasAlignment(0.0f, 0.0f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌎",
                fontSize = 42.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Places",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 48.sp,
                color = Color.White
            )
        }

        LinearProgressIndicator(
            modifier = Modifier.align(BiasAlignment(0.0f, 0.9f)),
            color = Color.White
        )
    }
}

// End Composables
