package com.adyen.android.assignment.ui.places.uiComponents

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adyen.android.assignment.ui.commonUi.Colors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesAppBar() {
    val configuration = LocalConfiguration.current

    TopAppBar(
        modifier = Modifier
            .requiredHeightIn(
                min = WindowInsets.safeContent.asPaddingValues().calculateTopPadding() +
                    WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding() +
                    68.dp // To be improved: Align this height with UX and Google Guidelines
            )
            .shadow(
                elevation = 8.dp
            ),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Colors.adyenGreenColor,
            titleContentColor = Color.White
        ),
        title = {
            val titleBaseModifier = Modifier.padding(
                top = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding() +
                    WindowInsets.safeContent.asPaddingValues().calculateTopPadding()
            )
            Text(
                modifier = if (configuration.orientation == ORIENTATION_LANDSCAPE) {
                    titleBaseModifier.padding(
                        start = WindowInsets.safeDrawing.asPaddingValues().calculateStartPadding(LayoutDirection.Ltr) +
                            WindowInsets.safeContent.asPaddingValues().calculateStartPadding(LayoutDirection.Ltr)
                    )
                } else {
                    titleBaseModifier
                },
                text = "Places Nearby You",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    )
}
