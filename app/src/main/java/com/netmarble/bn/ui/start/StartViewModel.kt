package com.netmarble.bn.ui.start

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netmarble.bn.R
import com.netmarble.bn.core.audio.BgmPlayer
import com.netmarble.bn.core.domain.model.Island
import com.netmarble.bn.core.domain.usecase.GetBalanceUseCase
import com.netmarble.bn.core.domain.usecase.GetDropsCountUseCase
import com.netmarble.bn.core.domain.usecase.GetIslandsUseCase
import com.netmarble.bn.ui.model.IslandUi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlin.math.min

data class StartUiState(
    val balance: Int = 0,
    val islands: List<IslandUi> = emptyList(),
    val selectedIndex: Int = 0,
    val isMusicOn: Boolean = true
)

@HiltViewModel
class StartViewModel @Inject constructor(
    getBalance: GetBalanceUseCase,
    getIslands: GetIslandsUseCase,
    getDropsCount: GetDropsCountUseCase,
    private val bgmPlayer: BgmPlayer,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val islandsInternal = MutableStateFlow(getIslands())
    private val selectedIndex = MutableStateFlow(0)
    private val musicToggle = MutableStateFlow(true)

    private val requirements: Map<String, Int> by lazy {
        mapOf(
            context.getString(R.string.island_ship_deck) to context.resources.getInteger(R.integer.requirement_ship_deck),
            context.getString(R.string.island_emerald_cave) to context.resources.getInteger(R.integer.requirement_emerald_cave),
            context.getString(R.string.island_volcanic_island) to context.resources.getInteger(R.integer.requirement_volcanic_island)
        )
    }

    val uiState: StateFlow<StartUiState> = combine(
        getBalance(),
        islandsInternal,
        selectedIndex,
        musicToggle,
        getDropsCount()
    ) { balance, islands, index, isMusicOn, drops ->
        val uiIslands = islands.toUi(drops)
        StartUiState(
            balance = balance,
            islands = uiIslands,
            selectedIndex = if (uiIslands.isEmpty()) 0 else index.coerceIn(0, uiIslands.lastIndex),
            isMusicOn = isMusicOn
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StartUiState()
    )

    init {
        bgmPlayer.play(R.raw.music)
    }

    fun onSelect(index: Int) {
        selectedIndex.value = index
    }

    fun toggleMusic() {
        musicToggle.value = !musicToggle.value
        bgmPlayer.setMusicEnabled(musicToggle.value)
        if (musicToggle.value) {
            bgmPlayer.play(R.raw.music)
        }
    }

    private fun List<Island>.toUi(drops: Int): List<IslandUi> = map { island ->
        val target = requirements[island.id] ?: 0
        IslandUi(
            id = island.id,
            title = island.title,
            artRes = island.artRes,
            locked = drops < target,
            progressCurrent = min(drops, target),
            progressTarget = target
        )
    }

    override fun onCleared() {
        bgmPlayer.release()
        super.onCleared()
    }
}