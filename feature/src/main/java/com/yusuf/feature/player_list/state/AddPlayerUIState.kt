package com.yusuf.feature.player_list.state

data class AddPlayerUIState(
    val isLoading: Boolean = false,
    val transaction: Boolean = false,
    val error: String? = null
)