package com.netmarble.bn.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface BalanceRepository {
    val balance: Flow<Int>
}