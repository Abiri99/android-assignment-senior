package com.adyen.android.assignment.ui.commonUi

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListItemTransitionEffect(
    key: Any,
    index: Int,
    modifier: Modifier = Modifier,
    translationEffectEnabled: Boolean = true,
    content: @Composable (Modifier, Float) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val offsetY = remember { Animatable(50f) }
    val opacity = remember { Animatable(0f) }
    val zIndex = remember { Animatable(0f) }

    LaunchedEffect(key1 = key) {
        delay(index * 50L) // Delay each item's animation by 30 milliseconds
        isVisible = true
        if (translationEffectEnabled) {
            coroutineScope.launch {
                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 500)
                )
            }
        }
        coroutineScope.launch {
            opacity.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500)
            )
        }
        zIndex.animateTo(
            targetValue = 8f,
            animationSpec = tween(durationMillis = 500)
        )
    }

    content(
        modifier.graphicsLayer {
            alpha = opacity.value
            translationY = if (translationEffectEnabled) offsetY.value else 0f
        },
        zIndex.value
    )
}
