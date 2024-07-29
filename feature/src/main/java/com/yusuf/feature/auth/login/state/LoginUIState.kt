package com.yusuf.feature.auth.login.state
import com.google.firebase.auth.FirebaseUser

data class LoginUiState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val error: String? = null,
)