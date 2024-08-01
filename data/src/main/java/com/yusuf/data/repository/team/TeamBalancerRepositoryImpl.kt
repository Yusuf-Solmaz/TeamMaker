package com.yusuf.data.repository.team

import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.repository.team.TeamBalancerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TeamBalancerRepositoryImpl @Inject constructor() : TeamBalancerRepository {
    override fun createBalancedTeams(players: List<PlayerData>): Flow<Pair<List<PlayerData>, List<PlayerData>>> = flow {
        if (players.size % 2 != 0) {
            throw IllegalArgumentException("Number of players must be even")
        }

        val teamSize = players.size / 2
        val sortedPlayers = players.sortedByDescending { it.skillRating }
        val teamFirst = mutableListOf<PlayerData>()
        val teamSecond = mutableListOf<PlayerData>()

        for (player in sortedPlayers) {
            if (teamFirst.sumOf { it.skillRating } < teamSecond.sumOf { it.skillRating }) {
                teamFirst.add(player)
            } else {
                teamSecond.add(player)
            }
        }
        emit(teamFirst to teamSecond)
    }.flowOn(Dispatchers.IO)
}