package com.netmarble.bn.core.domain.repository

import com.netmarble.bn.core.domain.model.BallSkin
import kotlinx.coroutines.flow.Flow

interface ShopRepository {
    fun catalog(): Flow<List<BallSkin>>
    fun owned(): Flow<Set<String>>
    fun selected(): Flow<String?>
    suspend fun buy(id: String)
    suspend fun equip(id: String)
}