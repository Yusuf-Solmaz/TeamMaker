package com.yusuf.domain.model.firebase

import java.util.UUID

data class CompetitionData(
    val competitionId: String = UUID.randomUUID().toString(),
    val competitionName: String,
    val competitionImageUrl: String
)