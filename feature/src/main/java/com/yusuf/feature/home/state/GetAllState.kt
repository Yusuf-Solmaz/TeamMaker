package com.yusuf.feature.home.state

import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.util.RootResult

data class GetAllState(
    val isLoading: Boolean = false,
    val competitions: List<CompetitionData> = emptyList(),
    val result: RootResult<List<CompetitionData>>? = null,
    val error: String? = null
)