package com.yusuf.domain.repository.team

import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.util.RootResult
import kotlinx.coroutines.flow.Flow

interface TeamBalancerRepository {
    fun createBalancedTeams(players: List<PlayerData>): Flow<RootResult<Pair<List<PlayerData>, List<PlayerData>>>>
    fun calculateTeamAverageSkillRating(players: List<PlayerData>): Double
}