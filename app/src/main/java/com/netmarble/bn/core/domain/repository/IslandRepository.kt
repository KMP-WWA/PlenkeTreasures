package com.netmarble.bn.core.domain.repository

import com.netmarble.bn.core.domain.model.Island


interface IslandRepository {
    fun getIslands(): List<Island>
}