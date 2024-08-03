package com.yusuf.feature.create_competition.select_player.state

import com.yusuf.domain.model.firebase.PlayerData

data class TeamBalancerUIState(
    val isLoading: Boolean = false,
    val teams: Pair<List<PlayerData>, List<PlayerData>>? = null,
    val errorMessage: String? = null
)
