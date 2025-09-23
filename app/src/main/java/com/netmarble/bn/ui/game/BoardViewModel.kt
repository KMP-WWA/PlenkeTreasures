package com.netmarble.bn.ui.game

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netmarble.bn.core.domain.model.PhysicsConfig
import com.netmarble.bn.core.domain.service.Peg
import com.netmarble.bn.core.domain.service.PlinkoEngine
import com.netmarble.bn.core.domain.service.Slot
import com.netmarble.bn.core.domain.usecase.AddCoinsUseCase
import com.netmarble.bn.core.domain.usecase.GetBalanceUseCase
import com.netmarble.bn.core.domain.usecase.GetPhysicsConfigUseCase
import com.netmarble.bn.core.domain.usecase.IncrementDropsUseCase
import com.netmarble.bn.core.domain.usecase.ObserveSelectedBallIconUseCase
import com.netmarble.bn.core.domain.usecase.SpendCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.random.Random

data class BoardUiState(
    val width: Float = 0f,
    val height: Float = 0f,
    val config: PhysicsConfig? = null,
    val pegs: List<Peg> = emptyList(),
    val slots: List<Slot> = emptyList(),
    val position: Offset = Offset.Zero,
    val velocity: Offset = Offset.Zero,
    val running: Boolean = false,
    val resultSlot: Int? = null,
    val ballIconRes: Int? = null,
    val ballVisible: Boolean = true,
    val bet: Int = 10,
    val balance: Int = 0,
    val canAfford: Boolean = true
)

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val getPhysicsConfig: GetPhysicsConfigUseCase,
    private val engine: PlinkoEngine,
    private val incrementDrops: IncrementDropsUseCase,
    private val spendCoins: SpendCoinsUseCase,
    private val addCoins: AddCoinsUseCase,
    getBalance: GetBalanceUseCase,
    observeSelectedBallIcon: ObserveSelectedBallIconUseCase
) : ViewModel() {

    private val _ui = MutableStateFlow(BoardUiState())
    val ui: StateFlow<BoardUiState> = _ui

    private var loopJob: Job? = null
    private val rng: Random = Random.Default

    init {
        viewModelScope.launch {
            getBalance().collectLatest { coins ->
                _ui.value = _ui.value.copy(
                    balance = coins,
                    canAfford = coins >= _ui.value.bet && !_ui.value.running
                )
            }
        }
        viewModelScope.launch {
            observeSelectedBallIcon().collectLatest { iconRes ->
                _ui.value = _ui.value.copy(ballIconRes = iconRes)
            }
        }
    }

    fun init(islandId: String) {
        val cfg = getPhysicsConfig(islandId)
        _ui.value = _ui.value.copy(config = cfg)
    }

    fun onMeasured(width: Float, height: Float) {
        val cfg = _ui.value.config ?: return
        if (width == _ui.value.width && height == _ui.value.height) return
        val pegs = engine.generatePegs(width, height)
        val slots = engine.generateSlots(width)
        val pegRadius = min(width, height) * cfg.pegRadiusFactor
        val ballRadius = pegRadius * cfg.ballRadiusFactor
        _ui.value = _ui.value.copy(
            width = width,
            height = height,
            pegs = pegs,
            slots = slots,
            position = Offset(width * 0.5f, ballRadius + 2f),
            velocity = Offset.Zero
        )
    }

    fun incBet() {
        val maxBet = _ui.value.balance.coerceAtMost(1000)
        _ui.value = _ui.value.copy(
            bet = maxBet,
            canAfford = _ui.value.balance >= maxBet && !_ui.value.running
        )
    }

    fun decBet() {
        val next = (_ui.value.bet - 10).coerceAtLeast(10)
        _ui.value = _ui.value.copy(
            bet = next,
            canAfford = _ui.value.balance >= next && !_ui.value.running
        )
    }

    fun onDropCommitted() {
        viewModelScope.launch { incrementDrops() }
    }

    fun drop() {
        val cfg = _ui.value.config ?: return
        if (_ui.value.running) return
        val width = _ui.value.width
        val height = _ui.value.height
        if (width <= 0f || height <= 0f) return
        val pegs = _ui.value.pegs
        val slots = _ui.value.slots
        val bet = _ui.value.bet
        viewModelScope.launch {
            val ok = spendCoins(bet)
            if (!ok) {
                _ui.value = _ui.value.copy(canAfford = false)
                return@launch
            }
            _ui.value = _ui.value.copy(running = true, resultSlot = null, canAfford = false, ballVisible = true)
            loopJob?.cancel()
            loopJob = launch {
                engine.run(
                    cfg = cfg,
                    width = width,
                    height = height,
                    pegs = pegs,
                    slots = slots,
                    rng = rng,
                    onPosition = { _ui.value = _ui.value.copy(position = it) },
                    onVelocity = { _ui.value = _ui.value.copy(velocity = it) },
                    onFinish = { idx ->
                        val slot = _ui.value.slots.getOrNull(idx)
                        val reward = if (slot != null) (_ui.value.bet * slot.multiplier).toInt() else 0
                        viewModelScope.launch {
                            if (reward > 0) addCoins(reward)
                        }
                        _ui.value = _ui.value.copy(
                            running = false,
                            resultSlot = idx,
                            canAfford = _ui.value.balance >= _ui.value.bet,
                            ballVisible = false
                        )
                    }
                )
            }
        }
    }

    fun pause() {
        loopJob?.cancel()
        _ui.value = _ui.value.copy(running = false, canAfford = _ui.value.balance >= _ui.value.bet)
    }

    fun reset() {
        pause()
        val cfg = _ui.value.config ?: return
        val width = _ui.value.width
        val height = _ui.value.height
        val pegRadius = min(width, height) * cfg.pegRadiusFactor
        val ballRadius = pegRadius * cfg.ballRadiusFactor
        _ui.value = _ui.value.copy(
            position = Offset(width * 0.5f, ballRadius + 2f),
            velocity = Offset.Zero,
            resultSlot = null
        )
    }
}