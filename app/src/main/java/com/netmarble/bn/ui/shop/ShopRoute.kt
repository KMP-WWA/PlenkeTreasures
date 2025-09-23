package com.netmarble.bn.ui.shop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ShopRoute(
    onBack: () -> Unit,
    viewModel: ShopViewModel = hiltViewModel()
) {
    val state = viewModel.ui.collectAsState().value
    ShopScreen(
        state = state,
        onBuy = viewModel::onBuy,
        onEquip = viewModel::onEquip,
        onBack = onBack
    )
}