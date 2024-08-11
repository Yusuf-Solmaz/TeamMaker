package com.yusuf.data.mapper.firebase_mapper

import com.yusuf.data.remote.dto.firebase_dto.SavedCompetitionsDto
import com.yusuf.domain.model.firebase.SavedCompetitionsModel

fun SavedCompetitionsDto.toSavedCompetitions(): SavedCompetitionsModel {
    return SavedCompetitionsModel(
        competitionId = competitionId,
        firstTeam = firstTeam,
        secondTeam = secondTeam,
        imageUrl = imageUrl,
        competitionTime = competitionTime,
        competitionDate = competitionDate,
        locationName = locationName,
        weatherModel = weatherModel,
        competitionName = competitionName
    )
}

fun SavedCompetitionsModel.toSavedCompetitionsDto(): SavedCompetitionsDto {
    return SavedCompetitionsDto(
        competitionId = competitionId,
        firstTeam = firstTeam,
        secondTeam = secondTeam,
        imageUrl = imageUrl,
        competitionTime = competitionTime,
        competitionDate = competitionDate,
        locationName = locationName,
        weatherModel = weatherModel,
        competitionName = competitionName
    )
}