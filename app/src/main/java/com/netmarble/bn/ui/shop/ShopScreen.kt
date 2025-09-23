package com.netmarble.bn.ui.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.netmarble.bn.R
import com.netmarble.bn.core.domain.usecase.ObserveShopStateUseCase
import com.netmarble.bn.ui.theme.Marcellus
import com.netmarble.bn.ui.theme.Gold

@Composable
fun ShopScreen(
    state: ObserveShopStateUseCase.State,
    onBuy: (String) -> Unit,
    onEquip: (String) -> Unit,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.start_screen_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_back),
                        contentDescription = "Back",
                        modifier = Modifier
                            .size(width = 60.dp, height = 40.dp)
                            .clickable { onBack() }
                    )

                    Text(
                        text = "TREASURE SHOP",
                        fontFamily = Marcellus,
                        color = Gold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.size(width = 60.dp, height = 40.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Coins: ${state.balance}",
                    fontFamily = Marcellus,
                    color = Gold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(state.items, key = { it.skin.id }) { item ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .shadow(12.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp),
                        color = Color.Transparent
                    ) {
                        Box {
                            Image(
                                painter = painterResource(R.drawable.ic_backshop),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.fillMaxSize()
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Spacer(modifier = Modifier.height(4.dp))

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.Black.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(top = 24.dp)
                                ) {
                                    Text(
                                        text = item.skin.name,
                                        fontFamily = Marcellus,
                                        color = Gold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Image(
                                    painter = painterResource(id = item.skin.iconRes),
                                    contentDescription = item.skin.name,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.size(90.dp)
                                )

                                Text(
                                    text = "${item.skin.price} coins",
                                    fontFamily = Marcellus,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                when {
                                    item.selected -> {
                                        Image(
                                            painter = painterResource(R.drawable.ic_equipped),
                                            contentDescription = "Selected",
                                            modifier = Modifier.size(width = 120.dp, height = 38.dp)
                                        )
                                    }
                                    item.owned -> {
                                        Image(
                                            painter = painterResource(R.drawable.ic_equip),
                                            contentDescription = "Equip",
                                            modifier = Modifier
                                                .size(width = 120.dp, height = 38.dp)
                                                .clickable { onEquip(item.skin.id) }
                                        )
                                    }
                                    else -> {
                                        Image(
                                            painter = painterResource(R.drawable.ic_buy_btn),
                                            contentDescription = if (item.canBuy) "Buy" else "Not enough",
                                            modifier = Modifier
                                                .size(width = 120.dp, height = 38.dp)
                                                .clickable {
                                                    if (item.canBuy) onBuy(item.skin.id)
                                                }
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}