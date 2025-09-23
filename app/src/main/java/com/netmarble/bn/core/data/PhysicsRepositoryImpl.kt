package com.netmarble.bn.core.data

import com.netmarble.bn.core.domain.model.PhysicsConfig
import com.netmarble.bn.core.domain.repository.PhysicsRepository

class PhysicsRepositoryImpl : PhysicsRepository {
    override fun getConfig(islandId: String): PhysicsConfig = when (islandId) {
        "emerald_cave" -> PhysicsConfig(
            pegRadiusFactor = 0.0115f,
            ballRadiusFactor = 3.5f,
            gravityFactor = 0.0011f,
            damping = 0.79f,
            substeps = 3,
            restitutionMin = 0.92f,
            restitutionMax = 1.00f,
            tangentKickFactor = 0.0018f,
            normalBoostFactor = 0.75f,
            angleMinDeg = 10f,
            angleMaxDeg = 22f
        )
        "volcanic_isle" -> PhysicsConfig(
            pegRadiusFactor = 0.0125f,
            ballRadiusFactor = 3.2f,
            gravityFactor = 0.00125f,
            damping = 0.82f,
            substeps = 4,
            restitutionMin = 0.96f,
            restitutionMax = 1.06f,
            tangentKickFactor = 0.0022f,
            normalBoostFactor = 0.9f,
            angleMinDeg = 14f,
            angleMaxDeg = 26f
        )
        else -> PhysicsConfig(
            pegRadiusFactor = 0.012f,
            ballRadiusFactor = 3.35f,
            gravityFactor = 0.0010f,
            damping = 0.795f,
            substeps = 3,
            restitutionMin = 0.95f,
            restitutionMax = 1.03f,
            tangentKickFactor = 0.0016f,
            normalBoostFactor = 0.6f,
            angleMinDeg = 12f,
            angleMaxDeg = 18f
        )
    }
}