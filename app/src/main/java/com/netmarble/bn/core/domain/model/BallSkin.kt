package com.netmarble.bn.core.domain.model

import androidx.annotation.DrawableRes

data class BallSkin(
    val id: String,
    val name: String,
    val price: Int,
    val rarity: Rarity,
    @DrawableRes val iconRes: Int
) {
    enum class Rarity { COMMON, RARE, EPIC, LEGENDARY }
}