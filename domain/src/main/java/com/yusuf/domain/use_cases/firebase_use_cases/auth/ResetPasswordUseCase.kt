package com.yusuf.domain.use_cases.firebase_use_cases.auth

import com.yusuf.domain.repository.firebase.auth.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) = authRepository.sendPasswordResetEmail(email)
}