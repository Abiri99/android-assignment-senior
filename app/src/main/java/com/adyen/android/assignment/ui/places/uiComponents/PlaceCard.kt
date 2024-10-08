package com.adyen.android.assignment.ui.places.uiComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adyen.android.assignment.data.api.model.Place
import com.adyen.android.assignment.ui.commonUi.BouncyEffect

@Composable
fun PlaceCard(
    modifier: Modifier = Modifier,
    shadow: Float,
    place: Place
) {
    BouncyEffect(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    shape = RoundedCornerShape(8.dp)
                    clip = false
                    shadowElevation = shadow
                }
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxHeight().weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = place.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = place.categories.fold("") { acc, category ->
                        if (acc == "") {
                            category.name
                        } else {
                            acc + ", " + category.name
                        }
                    },
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(Modifier.width(24.dp))

            Text(
                text = "${place.distance}m away",
                fontSize = 14.sp
            )
        }
    }
}
