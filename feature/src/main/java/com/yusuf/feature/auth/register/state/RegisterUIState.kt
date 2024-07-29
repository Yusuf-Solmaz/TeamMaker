package com.yusuf.feature.auth.register.state

import com.google.firebase.auth.FirebaseUser

data class RegisterUIState(
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val error: String? = null,
)