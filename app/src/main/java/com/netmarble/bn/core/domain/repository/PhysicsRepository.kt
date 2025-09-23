package com.netmarble.bn.core.domain.repository

import com.netmarble.bn.core.domain.model.PhysicsConfig

interface PhysicsRepository {
    fun getConfig(islandId: String): PhysicsConfig
}