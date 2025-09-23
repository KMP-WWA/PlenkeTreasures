package com.netmarble.bn.core.domain.usecase

import com.netmarble.bn.core.domain.repository.WalletRepository
import javax.inject.Inject

class AddCoinsUseCase @Inject constructor(
    private val wallet: WalletRepository
) {
    suspend operator fun invoke(amount: Int) = wallet.add(amount)
}