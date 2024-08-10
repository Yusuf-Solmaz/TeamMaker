package com.yusuf.feature.create_competition.select_player.state

data class TeamImageUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val imageUri: String? = null
)