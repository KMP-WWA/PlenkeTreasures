package com.netmarble.bn.ui.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netmarble.bn.core.domain.usecase.BuySkinUseCase
import com.netmarble.bn.core.domain.usecase.EquipSkinUseCase
import com.netmarble.bn.core.domain.usecase.ObserveShopStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ShopViewModel @Inject constructor(
    observe: ObserveShopStateUseCase,
    private val buy: BuySkinUseCase,
    private val equip: EquipSkinUseCase
) : ViewModel() {

    val ui: StateFlow<ObserveShopStateUseCase.State> =
        observe().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ObserveShopStateUseCase.State(0, emptyList()))

    fun onBuy(id: String) = viewModelScope.launch { buy(id) }
    fun onEquip(id: String) = viewModelScope.launch { equip(id) }
}