package com.netmarble.bn.core.domain.usecase

import com.netmarble.bn.core.domain.model.Island
import com.netmarble.bn.core.domain.repository.IslandRepository
import javax.inject.Inject

class GetIslandsUseCase @Inject constructor(
    private val repository: IslandRepository
) {
    operator fun invoke(): List<Island> = repository.getIslands()
}