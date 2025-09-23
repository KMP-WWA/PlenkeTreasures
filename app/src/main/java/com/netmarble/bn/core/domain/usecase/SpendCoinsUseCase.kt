package com.netmarble.bn.core.domain.usecase

import com.netmarble.bn.core.domain.repository.WalletRepository
import javax.inject.Inject

class SpendCoinsUseCase @Inject constructor(
    private val wallet: WalletRepository
) {
    suspend operator fun invoke(amount: Int): Boolean = wallet.spend(amount)
}