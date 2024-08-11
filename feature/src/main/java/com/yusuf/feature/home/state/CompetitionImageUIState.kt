package com.yusuf.feature.home.state

data class CompetitionImageUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val imageUri: String? = null
)
