package com.netmarble.bn.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface WalletRepository {
    fun balance(): Flow<Int>
    suspend fun add(amount: Int)
    suspend fun spend(amount: Int): Boolean
}