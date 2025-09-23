package com.netmarble.bn.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.netmarble.bn.R
import com.netmarble.bn.core.domain.model.BallSkin
import com.netmarble.bn.core.domain.repository.ShopRepository
import com.netmarble.bn.core.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.shopDataStore: DataStore<Preferences> by preferencesDataStore(name = "shop_prefs")

class ShopRepositoryImpl(
    private val context: Context,
    private val wallet: WalletRepository
) : ShopRepository {

    private val keyOwned = stringSetPreferencesKey("owned_skins")
    private val keySelected = stringPreferencesKey("selected_skin")

    private val baseCatalog = listOf(
        BallSkin("emerald", "Emerald", 200, BallSkin.Rarity.RARE, R.drawable.ic_emerald),
        BallSkin("ruby", "Ruby", 250, BallSkin.Rarity.RARE, R.drawable.ic_ruby),
        BallSkin("topaz", "Topaz", 150, BallSkin.Rarity.COMMON, R.drawable.ic_topaz),
        BallSkin("amethyst", "Amethyst", 220, BallSkin.Rarity.RARE, R.drawable.ic_amethyst),
        BallSkin("tourmaline", "Tourmaline", 350, BallSkin.Rarity.LEGENDARY, R.drawable.ic_tourmaline)
    )

    override fun catalog(): Flow<List<BallSkin>> = context.shopDataStore.data.map { baseCatalog }

    override fun owned(): Flow<Set<String>> = context.shopDataStore.data.map { it[keyOwned] ?: emptySet() }

    override fun selected(): Flow<String?> = context.shopDataStore.data.map { it[keySelected] }

    override suspend fun buy(id: String) {
        val skins = baseCatalog.associateBy { it.id }
        val price = skins[id]?.price ?: return
        val has = owned().map { it.contains(id) }.first()
        if (has) return
        val ok = wallet.spend(price)
        if (!ok) return
        context.shopDataStore.edit { prefs ->
            val cur = prefs[keyOwned] ?: emptySet()
            prefs[keyOwned] = cur + id
            if (prefs[keySelected].isNullOrEmpty()) prefs[keySelected] = id
        }
    }

    override suspend fun equip(id: String) {
        val owns = owned().map { it.contains(id) }.first()
        if (!owns) return
        context.shopDataStore.edit { it[keySelected] = id }
    }

}