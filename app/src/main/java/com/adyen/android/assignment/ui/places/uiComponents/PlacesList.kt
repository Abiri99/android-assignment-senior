package com.adyen.android.assignment.ui.places.uiComponents

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adyen.android.assignment.data.api.model.Place
import com.adyen.android.assignment.ui.commonUi.Colors
import com.adyen.android.assignment.ui.commonUi.ListItemTransitionEffect

// TODO: Improve rendering performance
@Composable
fun PlacesList(
    modifier: Modifier = Modifier,
    items: Map<String, List<Place>>
) {
    val configuration = LocalConfiguration.current

    LazyColumn(
        modifier = if (configuration.orientation == ORIENTATION_LANDSCAPE) {
            modifier.padding(
                start = WindowInsets.safeDrawing.asPaddingValues().calculateStartPadding(LayoutDirection.Ltr) +
                    WindowInsets.safeContent.asPaddingValues().calculateStartPadding(LayoutDirection.Ltr),
                end = WindowInsets.safeDrawing.asPaddingValues().calculateEndPadding(LayoutDirection.Ltr) +
                    WindowInsets.safeContent.asPaddingValues().calculateEndPadding(LayoutDirection.Ltr)
            )
        } else {
            modifier
        },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }

        items.entries.forEach { entry ->
            item {
                ListItemTransitionEffect(
                    key = entry.key + entry.value.size,
                    translationEffectEnabled = false,
                    index = 0
                ) { modifier, _ ->
                    Text(
                        modifier = modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        text = entry.key,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Colors.textColorDark
                    )
                }
            }

            itemsIndexed(
                entry.value,
                key = { index, _ -> entry.value[index].fsq_id + entry.key + (31 * index) }
            ) { index, place ->
                ListItemTransitionEffect(
                    key = index,
                    index = index
                ) { modifier, zIndex ->
                    PlaceCard(
                        modifier = modifier.then(
                            Modifier
                                .fillMaxWidth()
                                .height(82.dp)
                                .padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                )
                        ),
                        shadow = zIndex,
                        place = place
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        item {
            Spacer(Modifier.height(WindowInsets.safeContent.asPaddingValues().calculateBottomPadding() + 16.dp))
        }
    }
}
