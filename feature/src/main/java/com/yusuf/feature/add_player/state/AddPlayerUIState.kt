package com.yusuf.feature.add_player.state

data class AddPlayerUIState(
    val isLoading: Boolean = false,
    val transaction: Boolean = false,
    val error: String? = null
)