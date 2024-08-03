package com.yusuf.feature.match_detail.team_balancer

import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.util.RootResult

data class TeamBalancerUIState(
    val isLoading: Boolean = false,
    val teams: RootResult<Pair<List<PlayerData>, List<PlayerData>>>? = null,
    val errorMessage: String? = null
)
