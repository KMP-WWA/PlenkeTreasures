package com.netmarble.bn.ui.model

data class IslandUi(
    val id: String,
    val title: String,
    val artRes: Int,
    val locked: Boolean,
    val progressCurrent: Int,
    val progressTarget: Int
)

