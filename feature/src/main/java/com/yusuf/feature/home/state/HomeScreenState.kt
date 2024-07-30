package com.yusuf.feature.home.state

import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.util.RootResult

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val transaction: Boolean = false,
    val competitions: List<CompetitionData> = emptyList(), // Use this for displaying
    val addCompetitionResult: RootResult<Boolean>? = null,
    val deleteCompetitionResult: RootResult<Boolean>? = null,
    val getAllCompetitionsResult: RootResult<List<CompetitionData>>? = null
)
