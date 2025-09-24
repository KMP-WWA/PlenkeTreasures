package com.netmarble.bn.ui.start

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.netmarble.bn.R
import com.netmarble.bn.ui.components.DotsIndicator
import com.netmarble.bn.ui.components.IslandCard
import com.netmarble.bn.ui.components.PressableButton
import com.netmarble.bn.ui.components.RulesDialog
import com.netmarble.bn.ui.model.IslandUi
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun StartScreen(
    balance: Int,
    islands: List<IslandUi>,
    selectedIndex: Int,
    isMusicOn: Boolean,
    onSelect: (Int) -> Unit,
    onPlay: () -> Unit,
    onToggleMusic: () -> Unit,
    onShop: () -> Unit
) {
    var showRules by remember { mutableStateOf(false) }

    if (showRules) {
        RulesDialog(onDismiss = { showRules = false })
    }

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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp)
                    .size(width = 220.dp, height = 150.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            val pagerState = rememberPagerState(
                initialPage = selectedIndex.coerceIn(0, (islands.size - 1).coerceAtLeast(0)),
                pageCount = { islands.size }
            )
            val scope = rememberCoroutineScope()

            LaunchedEffect(selectedIndex, islands.size) {
                if (islands.isNotEmpty()) {
                    val target = selectedIndex.coerceIn(0, islands.lastIndex)
                    if (pagerState.currentPage != target) pagerState.scrollToPage(target)
                }
            }
            LaunchedEffect(pagerState.currentPage) {
                if (islands.isNotEmpty() && pagerState.currentPage != selectedIndex) {
                    onSelect(pagerState.currentPage.coerceIn(0, islands.lastIndex))
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 48.dp),
                pageSize = PageSize.Fill,
                pageSpacing = 16.dp
            ) { page ->
                val pageOffset = (pagerState.currentPage - page).toFloat().absoluteValue
                val scale = 0.9f + (1f - pageOffset.coerceIn(0f, 1f)) * 0.1f
                val item = islands[page]
                IslandCard(
                    title = item.title,
                    artRes = item.artRes,
                    locked = item.locked,
                    progressCurrent = item.progressCurrent,
                    progressTarget = item.progressTarget,
                    scale = scale,
                    onClick = { scope.launch { pagerState.animateScrollToPage(page) } }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            DotsIndicator(
                total = islands.size,
                selectedIndex = pagerState.currentPage.coerceIn(0, (islands.size - 1).coerceAtLeast(0)),
                onDotClick = { index ->
                    scope.launch {
                        pagerState.animateScrollToPage(index.coerceIn(0, (islands.size - 1).coerceAtLeast(0)))
                    }
                },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            PressableButton(
                modifier = Modifier.size(width = 220.dp, height = 66.dp),
                onClick = {
                    if (islands.getOrNull(pagerState.currentPage)?.locked == false) {
                        onPlay()
                    }
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_play_btn),
                    contentDescription = stringResource(R.string.play),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            PressableButton(
                modifier = Modifier.size(width = 200.dp, height = 66.dp),
                onClick = onShop
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_shop_btn),
                    contentDescription = stringResource(R.string.shop),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                PressableButton(
                    modifier = Modifier.size(56.dp),
                    onClick = onToggleMusic
                ) {
                    Image(
                        painter = painterResource(if (isMusicOn) R.drawable.ic_music_on else R.drawable.ic_music_off),
                        contentDescription = if (isMusicOn) stringResource(R.string.music_on) else stringResource(
                            R.string.music_off
                        ),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                PressableButton(
                    modifier = Modifier.size(56.dp),
                    onClick = { showRules = true }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_rules_btn),
                        contentDescription = stringResource(R.string.rules),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
