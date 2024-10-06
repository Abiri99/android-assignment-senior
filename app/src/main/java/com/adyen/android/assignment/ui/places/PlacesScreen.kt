package com.adyen.android.assignment.ui.places

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adyen.android.assignment.ui.common_ui.Colors
import com.adyen.android.assignment.data.api.model.Place

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    getCachedPlacesCallback: () -> List<Place>,
) {
    val places = remember(Unit) { getCachedPlacesCallback() }

    if (places.isNotEmpty()) {
        PlacesList(
            modifier,
            list = places,
        )
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            Text("No places found!")
        }
    }
}

@Composable
private fun PlacesList(
    modifier: Modifier = Modifier,
    list: List<Place>,
) {
    val contentModifier = Modifier.padding(
        horizontal = 16.dp,
        vertical = 8.dp,
    )

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item(key = 1) {
            Text(
                text = "Place nearby",
                color = Colors.textColorDark,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = contentModifier,
            )
        }

        items(
            list.size,
            key = { index -> list[index].fsq_id },
        ) { index ->
            val place = list[index]

            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 16.dp,
                ),
            ) {
                Text(place.name)
            }
        }
    }
}
