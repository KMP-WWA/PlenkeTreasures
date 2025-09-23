package com.netmarble.bn.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BoardRoute(
    islandId: String,
    viewModel: BoardViewModel = hiltViewModel()
) {
    LaunchedEffect(islandId) { viewModel.init(islandId) }
    val state = viewModel.ui.collectAsState().value

    BoardScreen(
        islandId = islandId,
        state = state,
        onMeasured = viewModel::onMeasured,
        onDrop = viewModel::drop,
        onPause = viewModel::pause,
        onDropCommitted = viewModel::onDropCommitted,
        onIncBet = viewModel::incBet,
        onDecBet = viewModel::decBet
    )
}