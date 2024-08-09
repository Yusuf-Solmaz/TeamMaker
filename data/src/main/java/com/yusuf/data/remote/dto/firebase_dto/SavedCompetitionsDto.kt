package com.yusuf.data.remote.dto.firebase_dto

import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.model.weather.CurrentWeatherModel
import java.util.UUID

data class SavedCompetitionsDto(
    val competitionId: String = UUID.randomUUID().toString(),
    val firstTeam: List<PlayerData>? = null,
    val secondTeam: List<PlayerData>? = null,
    val imageUrl: String = "",
    val competitionTime: String = "",
    val competitionDate: String = "",
    val locationName: String = "",
    val weatherModel: CurrentWeatherModel? = null
)
