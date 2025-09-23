package com.netmarble.bn.core.domain.model

data class PhysicsConfig(
    val pegRadiusFactor: Float,
    val ballRadiusFactor: Float,
    val gravityFactor: Float,
    val damping: Float,
    val substeps: Int,
    val restitutionMin: Float,
    val restitutionMax: Float,
    val tangentKickFactor: Float,
    val normalBoostFactor: Float,
    val angleMinDeg: Float,
    val angleMaxDeg: Float
)