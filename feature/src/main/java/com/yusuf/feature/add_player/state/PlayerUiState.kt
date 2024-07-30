package com.yusuf.feature.add_player.state

import com.yusuf.domain.model.firebase.PlayerData

data class PlayerUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val playerList: List<PlayerData>? = emptyList()
)
