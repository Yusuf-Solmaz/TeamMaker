package com.yusuf.domain.repository.firebase.player

import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.util.RootResult
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    fun getAllPlayers(): Flow<RootResult<List<PlayerData>>>
}