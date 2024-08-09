package com.yusuf.domain.repository.firebase.player

import android.net.Uri
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.util.RootResult
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun getAllPlayers(): Flow<RootResult<List<PlayerData>>>
    suspend fun addPlayer(playerData: PlayerData,  imageUri: Uri): Flow<RootResult<Boolean>>
    suspend fun addCompetition(competitionData: CompetitionData): Flow<RootResult<Boolean>>
    suspend fun deleteCompetition(competitionId: String): Flow <RootResult<Boolean>>
    suspend fun getAllCompetitions(): Flow<RootResult<List<CompetitionData>>>
    fun getCurrentUserId(): Flow<RootResult<String?>>
    suspend fun deletePlayerById(playerId: String): Flow<RootResult<Boolean>>
    suspend fun updatePlayerById(playerId: String, updatedPlayerData: PlayerData) : Flow<RootResult<Boolean>>
    suspend fun uploadImage(uri: Uri): Flow<RootResult<String>>
    suspend fun updatePlayerImage(uri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit): Flow<RootResult<Boolean>>
    suspend fun getPlayersByCompetitionType(competitionType: String): Flow<RootResult<List<PlayerData>>>
    suspend fun updateCompetition(competitionId: String, competitionData: CompetitionData): Flow<RootResult<Boolean>>
    suspend fun deleteCurrentUser(): Flow<RootResult<Boolean>>
    suspend fun uploadImageCompetition(uri: Uri): Result<String>
}

