package com.yusuf.feature.playerList.state

data class AddPlayerUIState(
    val isLoading: Boolean = false,
    val transaction: Boolean = false,
    val error: String? = null
)