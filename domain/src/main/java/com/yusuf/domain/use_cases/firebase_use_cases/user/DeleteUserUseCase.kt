package com.yusuf.domain.use_cases.firebase_use_cases.user

import com.yusuf.domain.repository.firebase.auth.AuthRepository
import com.yusuf.domain.repository.firebase.player.PlayerRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val authRepository: PlayerRepository
) {
    suspend operator fun invoke() = authRepository.deleteCurrentUser()
}