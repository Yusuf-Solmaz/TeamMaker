package com.yusuf.domain.model.firebase

import java.util.UUID

data class SavedCompetitionsModel (
        val competitionId: String = UUID.randomUUID().toString(),
        val firstTeam: List<PlayerData>? = null,
        val secondTeam: List<PlayerData>? = null,
        val imageUrl: String = "",
        val competitionTime: String = "",
        val competitionDate: String = "",
        val locationName: String = "",
        val weather: String = ""
)