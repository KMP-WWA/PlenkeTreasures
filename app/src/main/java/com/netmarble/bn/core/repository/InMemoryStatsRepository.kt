package com.netmarble.bn.core.repository

import com.netmarble.bn.core.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryStatsRepository : StatsRepository {
    private val drops = MutableStateFlow(0)
    override val dropsCount: Flow<Int> = drops.asStateFlow()
    override suspend fun incrementDrops() {
        drops.value = drops.value + 1
    }
}