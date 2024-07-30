package com.yusuf.data.mapper

import com.yusuf.data.remote.dto.firebase_dto.CompetitionDataDto
import com.yusuf.domain.model.firebase.CompetitionData

fun CompetitionDataDto.toCompetitionData(): CompetitionData {
    return CompetitionData(
        competitionName = competitionName,
        competitionDescription = competitionDescription,
        competitionImageUrl = competitionImageUrl
    )
}

fun CompetitionData.toCompetitionDataDto(): CompetitionDataDto {
    return CompetitionDataDto(
        competitionName = competitionName,
        competitionDescription = competitionDescription,
        competitionImageUrl = competitionImageUrl
    )
}