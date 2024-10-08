package com.yusuf.domain.model.firebase

import com.yusuf.domain.model.weather.CurrentWeatherModel
import java.io.Serializable
import java.util.UUID

data class SavedCompetitionsModel (
        val competitionId: String = UUID.randomUUID().toString(),
        val firstTeam: List<PlayerData>? = null,
        val secondTeam: List<PlayerData>? = null,
        val imageUrl: String = "",
        val competitionTime: String = "",
        val competitionDate: String = "",
        val locationName: String = "",
        val weatherModel: CurrentWeatherModel? = null,
        val competitionName: String
) : Serializable