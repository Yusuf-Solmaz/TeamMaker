package com.yusuf.feature.auth.forgot_password.state

data class ForgotPasswordState(
    val transaction: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)