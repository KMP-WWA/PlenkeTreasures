package com.netmarble.bn.core.domain.usecase

import com.netmarble.bn.core.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val wallet: WalletRepository
) {
    operator fun invoke(): Flow<Int> = wallet.balance()
}