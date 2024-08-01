package com.yusuf.domain.repository.team

import com.yusuf.domain.model.firebase.PlayerData
import kotlinx.coroutines.flow.Flow

interface TeamBalancerRepository {
    fun createBalancedTeams(players: List<PlayerData>): Flow<Pair<List<PlayerData>, List<PlayerData>>>
}