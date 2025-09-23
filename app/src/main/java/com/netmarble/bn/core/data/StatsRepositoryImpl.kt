package com.netmarble.bn.core.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.netmarble.bn.core.domain.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.statsDataStore: DataStore<Preferences> by preferencesDataStore(name = "stats_prefs")

class StatsRepositoryImpl(
    private val context: Context
) : StatsRepository {

    private val keyDrops = intPreferencesKey("total_drops")

    override val dropsCount: Flow<Int> =
        context.statsDataStore.data.map { it[keyDrops] ?: 0 }

    override suspend fun incrementDrops() {
        context.statsDataStore.edit { prefs ->
            val cur = prefs[keyDrops] ?: 0
            prefs[keyDrops] = cur + 1
        }
    }
}