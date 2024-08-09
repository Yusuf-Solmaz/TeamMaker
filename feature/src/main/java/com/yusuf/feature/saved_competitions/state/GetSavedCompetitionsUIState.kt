package com.yusuf.feature.saved_competitions.state

import com.yusuf.domain.model.firebase.SavedCompetitionsModel

data class GetSavedCompetitionsUIState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val savedCompetitions: List<SavedCompetitionsModel>? = emptyList()
)
