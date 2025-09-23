package com.netmarble.bn.core.domain.usecase

import com.netmarble.bn.R
import com.netmarble.bn.core.domain.repository.ShopRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveSelectedBallIconUseCase @Inject constructor(
    private val shop: ShopRepository
) {
    operator fun invoke(): Flow<Int> = combine(
        shop.catalog(),
        shop.selected()
    ) { catalog, selectedId ->
        catalog.firstOrNull { it.id == selectedId }?.iconRes ?: R.drawable.ic_tourmaline
    }
}