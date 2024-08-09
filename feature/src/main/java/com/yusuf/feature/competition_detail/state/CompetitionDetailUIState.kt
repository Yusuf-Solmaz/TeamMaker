package com.yusuf.feature.competition_detail.state

data class CompetitionDetailUIState (
    val isLoading: Boolean = false,
    val transaction: Boolean = false,
    val error: String? = null
)