package com.yusuf.feature.saved_competitions.state

data class DeleteSavedCompetitionsUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val transaction: Boolean = false
)
