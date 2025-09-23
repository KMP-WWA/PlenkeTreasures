package com.netmarble.bn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netmarble.bn.ui.theme.Gold

@Composable
fun IslandCard(
    title: String,
    artRes: Int,
    locked: Boolean,
    progressCurrent: Int,
    progressTarget: Int,
    modifier: Modifier = Modifier,
    scale: Float = 1f,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(3f / 4f)
            .clipToBounds()
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
            }
    ) {
        Image(
            painter = painterResource(id = artRes),
            contentDescription = title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f)
                .alpha(if (locked) 0.7f else 1f)
        )

        if (locked) {
            Text(
                text = "🔒",
                style = MaterialTheme.typography.headlineLarge,
                color = Gold,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(40.dp)
        ) {
            Text(
                text = title,
                color = Gold,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            if (locked) {
                Text(
                    text = "Drops: $progressCurrent/$progressTarget",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Gold
                )
            }
        }
    }
}