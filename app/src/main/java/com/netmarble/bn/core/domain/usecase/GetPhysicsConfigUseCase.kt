package com.netmarble.bn.core.domain.usecase

import com.netmarble.bn.core.domain.model.PhysicsConfig
import com.netmarble.bn.core.domain.repository.PhysicsRepository
import javax.inject.Inject

class GetPhysicsConfigUseCase @Inject constructor(
    private val repository: PhysicsRepository
) {
    operator fun invoke(islandId: String): PhysicsConfig = repository.getConfig(islandId)
}