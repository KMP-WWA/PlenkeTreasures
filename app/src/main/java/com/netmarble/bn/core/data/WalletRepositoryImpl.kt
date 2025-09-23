package com.netmarble.bn.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.netmarble.bn.core.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.walletDataStore: DataStore<Preferences> by preferencesDataStore(name = "wallet_prefs")

class WalletRepositoryImpl(private val context: Context) : WalletRepository {

    private val keyBalance = intPreferencesKey("coins")

    override fun balance(): Flow<Int> = context.walletDataStore.data.map { it[keyBalance] ?: 1000 }

    override suspend fun add(amount: Int) {
        context.walletDataStore.edit { prefs ->
            val cur = prefs[keyBalance] ?: 1000
            prefs[keyBalance] = (cur + amount).coerceAtLeast(0)
        }
    }

    override suspend fun spend(amount: Int): Boolean {
        var ok = false
        context.walletDataStore.edit { prefs ->
            val cur = prefs[keyBalance] ?: 1000
            if (cur >= amount) {
                prefs[keyBalance] = cur - amount
                ok = true
            }
        }
        return ok
    }
}