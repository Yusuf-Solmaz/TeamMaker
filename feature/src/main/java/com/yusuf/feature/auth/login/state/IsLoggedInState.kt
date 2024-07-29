package com.yusuf.feature.auth.login.state

data class IsLoggedInState(
    val transaction: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)