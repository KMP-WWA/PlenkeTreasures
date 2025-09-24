package com.netmarble.bn.ui.start

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun StartRoute(
    onPlay: (String) -> Unit,
    onShop: () -> Unit,
    viewModel: StartViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()


    StartScreen(
        balance = state.balance,
        islands = state.islands,
        selectedIndex = state.selectedIndex,
        isMusicOn = state.isMusicOn,
        onSelect = viewModel::onSelect,
        onPlay = {
            val selected = state.islands.getOrNull(state.selectedIndex) ?: return@StartScreen
            if (!selected.locked) onPlay(selected.id)
        },
        onToggleMusic = viewModel::toggleMusic,
        onShop = onShop
    )
}