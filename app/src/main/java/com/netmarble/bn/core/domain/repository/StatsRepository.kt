package com.netmarble.bn.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    val dropsCount: Flow<Int>
    suspend fun incrementDrops()
}