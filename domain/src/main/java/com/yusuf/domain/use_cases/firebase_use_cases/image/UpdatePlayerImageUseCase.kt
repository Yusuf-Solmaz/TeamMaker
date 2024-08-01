package com.yusuf.domain.use_cases.firebase_use_cases.image

import android.net.Uri
import com.yusuf.domain.repository.firebase.player.PlayerRepository
import javax.inject.Inject

class UpdatePlayerImageUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(uri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) = playerRepository.updatePlayerImage(uri, onSuccess, onFailure)
}