package com.yusuf.feature.match_detail.team_balancer

import com.yusuf.domain.model.firebase.PlayerData

data class TeamBalancerUIState(
    val isLoading: Boolean = false,
    val teams: Pair<List<PlayerData>, List<PlayerData>>? = null,
    val errorMessage: String? = null
)
