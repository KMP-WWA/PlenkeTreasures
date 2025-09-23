package com.netmarble.bn.core.domain.usecase

import com.netmarble.bn.core.domain.model.BallSkin
import com.netmarble.bn.core.domain.repository.ShopRepository
import com.netmarble.bn.core.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveCatalogUseCase @Inject constructor(
    private val shop: ShopRepository
) {
    operator fun invoke(): Flow<List<BallSkin>> = shop.catalog()
}

class ObserveShopStateUseCase @Inject constructor(
    private val shop: ShopRepository,
    private val wallet: WalletRepository
) {
    data class Item(
        val skin: BallSkin,
        val owned: Boolean,
        val selected: Boolean,
        val canBuy: Boolean
    )
    data class State(
        val balance: Int,
        val items: List<Item>
    )
    operator fun invoke(): Flow<State> = combine(
        wallet.balance(),
        shop.catalog(),
        shop.owned(),
        shop.selected()
    ) { balance, catalog, owned, selected ->
        State(
            balance = balance,
            items = catalog.map { s ->
                val isOwned = owned.contains(s.id)
                Item(
                    skin = s,
                    owned = isOwned,
                    selected = selected == s.id,
                    canBuy = !isOwned && balance >= s.price
                )
            }
        )
    }
}

class BuySkinUseCase @Inject constructor(
    private val shop: ShopRepository
) {
    suspend operator fun invoke(id: String) = shop.buy(id)
}

class EquipSkinUseCase @Inject constructor(
    private val shop: ShopRepository
) {
    suspend operator fun invoke(id: String) = shop.equip(id)
}