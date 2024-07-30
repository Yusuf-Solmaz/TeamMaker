package com.yusuf.domain.repository.firebase.player

import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.util.RootResult
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun getAllPlayers(): Flow<RootResult<List<PlayerData>>>
    fun getCurrentUserId(): Flow<RootResult<String?>>
    suspend fun addPlayer(playerData: PlayerData): Flow<RootResult<Boolean>>
    suspend fun addCompetition(competitionData: CompetitionData): Flow<RootResult<Boolean>>
    suspend fun getAllCompetitions(): Flow<RootResult<List<CompetitionData>>>
}