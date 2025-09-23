package com.netmarble.bn.core.repository

import com.netmarble.bn.R
import com.netmarble.bn.core.domain.model.Island
import com.netmarble.bn.core.domain.repository.IslandRepository

class StaticIslandRepository : IslandRepository {
    override fun getIslands(): List<Island> = listOf(
        Island(id = "ship_deck", title = "Ship Deck", artRes = R.drawable.ic_ship_deck, locked = false),
        Island(id = "emerald_cave", title = "Emerald Cave", artRes = R.drawable.ic_cave_deck, locked = true),
        Island(id = "volcanic_island", title = "Volcanic Island", artRes = R.drawable.ic_volcanic_deck, locked = true)
    )
}