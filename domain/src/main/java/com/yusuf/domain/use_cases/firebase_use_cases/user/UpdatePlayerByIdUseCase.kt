package com.yusuf.domain.use_cases.firebase_use_cases.user

import android.net.Uri
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.repository.firebase.player.PlayerRepository
import javax.inject.Inject

class UpdatePlayerByIdUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(playerId: String, updatedPlayerData: PlayerData,imageUri:Uri) =
        playerRepository.updatePlayerById(playerId, updatedPlayerData,imageUri)
}