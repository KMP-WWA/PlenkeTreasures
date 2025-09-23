package com.netmarble.bn.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netmarble.bn.ui.components.BetControl
import com.netmarble.bn.ui.components.WinAnimationControlled
import com.netmarble.bn.ui.theme.Gold
import kotlin.math.min
import com.netmarble.bn.R

private fun bgForIsland(id: String) = when (id) {
    "ship_deck" -> R.drawable.ship_deck
    "emerald_cave" -> R.drawable.cave_deck
    "volcanic_island" -> R.drawable.volcanic_deck
    else -> R.drawable.ship_deck
}

private fun titleForIsland(id: String) = when (id) {
    "ship_deck" -> "Ship Deck"
    "emerald_cave" -> "Emerald Cave"
    "volcanic_island" -> "Volcanic Island"
    else -> "Ship Deck"
}

@Composable
fun BoardScreen(
    islandId: String,
    state: BoardUiState,
    onMeasured: (Float, Float) -> Unit,
    onDrop: () -> Unit,
    onPause: () -> Unit,
    onDropCommitted: () -> Unit,
    onIncBet: () -> Unit,
    onDecBet: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(bgForIsland(islandId)),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = titleForIsland(islandId),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gold,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Coins: ${state.balance}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gold,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                LaunchedEffect(constraints.maxWidth, constraints.maxHeight) {
                    onMeasured(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())
                }

                val pegRadius = min(state.width, state.height) * (state.config?.pegRadiusFactor ?: 0.012f)
                val ballRadius = pegRadius * (state.config?.ballRadiusFactor ?: 3.35f)
                val pegSizeDp = with(LocalDensity.current) { (pegRadius * 4f).toDp() }
                val renderScale = 1.0f
                val ballSizeDp = with(LocalDensity.current) { (ballRadius * 2f * renderScale).toDp() }

                Box(modifier = Modifier.fillMaxSize()) {
                    state.pegs.forEach { peg ->
                        Image(
                            painter = painterResource(R.drawable.ic_peg),
                            contentDescription = "Peg",
                            modifier = Modifier
                                .graphicsLayer {
                                    translationX = peg.center.x - (pegSizeDp.toPx() / 2f)
                                    translationY = peg.center.y - (pegSizeDp.toPx() / 2f)
                                }
                                .size(pegSizeDp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    if (state.ballIconRes != null && state.ballVisible) {
                        Image(
                            painter = painterResource(id = state.ballIconRes),
                            contentDescription = "Ball",
                            modifier = Modifier
                                .graphicsLayer {
                                    translationX = state.position.x - (ballRadius * renderScale)
                                    translationY = state.position.y - (ballRadius * renderScale)
                                }
                                .size(ballSizeDp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    val chestHeight = 64.dp
                    val chestWidthDp = with(LocalDensity.current) { (state.width / 6f).toDp() }
                    val bottomBarPadding = chestHeight + 24.dp

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = bottomBarPadding),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        BetControl(
                            bet = state.bet,
                            onDec = onDecBet,
                            onInc = onIncBet,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )

                        Button(
                            onClick = {
                                if (!state.running && state.canAfford) {
                                    onDropCommitted()
                                    onDrop()
                                }
                            },
                            enabled = state.canAfford && !state.running,
                            modifier = Modifier
                                .width(220.dp)
                                .height(34.dp)
                        ) {
                            Text(
                                "Drop Ball",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    state.resultSlot?.let {
                        val slot = state.slots.getOrNull(it)
                        val m = slot?.multiplier ?: 1.0
                        val winAmount = (state.bet * m).toInt()

                        WinAnimationControlled(
                            winAmount = winAmount,
                            multiplier = m,
                            trigger = true,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            state.slots.forEachIndexed { index, slot ->
                                val chestRes = when (slot.multiplier) {
                                    10.0 -> R.drawable.ic_chest_1
                                    3.0 -> R.drawable.ic_chest_2
                                    0.5 -> R.drawable.ic_chest_3
                                    0.3 -> R.drawable.ic_chest_3
                                    else -> R.drawable.ic_chest_3
                                }
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Image(
                                        painter = painterResource(chestRes),
                                        contentDescription = "Chest ×${slot.multiplier}",
                                        modifier = Modifier.size(
                                            width = chestWidthDp,
                                            height = chestHeight
                                        ),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}