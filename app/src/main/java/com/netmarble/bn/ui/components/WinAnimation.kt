package com.netmarble.bn.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.netmarble.bn.ui.theme.Marcellus
import com.netmarble.bn.ui.theme.Gold
import kotlinx.coroutines.delay

@Composable
fun WinAnimation(
    winAmount: Int,
    multiplier: Double,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    onAnimationEnd: () -> Unit = {}
) {
    val offsetY by animateFloatAsState(
        targetValue = if (isVisible) -400f else 0f,
        animationSpec = tween(
            durationMillis = 3000,
            easing = LinearEasing
        ),
        label = "offsetY"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = keyframes {
            durationMillis = 3000
            0f at 0
            1f at 300
            1f at 1500
            0f at 3000
        },
        label = "alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isVisible) 0.8f else 0.8f,
        animationSpec = keyframes {
            durationMillis = 3000
            0.8f at 0
            1.3f at 600
            1.0f at 3000
        },
        label = "scale"
    )

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(3000)
            onAnimationEnd()
        }
    }

    if (isVisible) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Won: $winAmount (×$multiplier)",
                fontFamily = Marcellus,
                fontSize = 24.sp,
                color = Gold,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .offset(y = offsetY.dp)
                    .alpha(alpha)
                    .scale(scale)
            )
        }
    }
}

@Composable
fun WinAnimationControlled(
    winAmount: Int,
    multiplier: Double,
    trigger: Boolean,
    modifier: Modifier = Modifier,
    onAnimationComplete: () -> Unit = {}
) {
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(trigger) {
        if (trigger && !isAnimating) {
            isAnimating = true
        }
    }

    WinAnimation(
        winAmount = winAmount,
        multiplier = multiplier,
        isVisible = isAnimating,
        modifier = modifier,
        onAnimationEnd = {
            isAnimating = false
            onAnimationComplete()
        }
    )
}
