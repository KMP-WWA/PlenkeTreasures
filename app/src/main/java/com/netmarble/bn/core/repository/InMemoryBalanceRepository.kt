package com.netmarble.bn.core.repository

import com.netmarble.bn.core.domain.repository.BalanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryBalanceRepository : BalanceRepository {
    private val internal = MutableStateFlow(1000)
    override val balance: Flow<Int> = internal.asStateFlow()
}