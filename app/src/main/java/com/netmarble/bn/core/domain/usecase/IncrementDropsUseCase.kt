package com.netmarble.bn.core.domain.usecase

import com.netmarble.bn.core.domain.repository.StatsRepository
import javax.inject.Inject

class IncrementDropsUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    suspend operator fun invoke() = repository.incrementDrops()
}