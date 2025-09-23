package com.netmarble.bn.core.domain.usecase

import com.netmarble.bn.core.domain.repository.StatsRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetDropsCountUseCase @Inject constructor(
    private val repository: StatsRepository
) {
    operator fun invoke(): Flow<Int> = repository.dropsCount
}