package com.yusuf.data.mapper

import com.yusuf.data.remote.dto.firebase_dto.CompetitionDataDto
import com.yusuf.domain.model.firebase.CompetitionData

fun CompetitionDataDto.toCompetitionData(): CompetitionData {
    return CompetitionData(
        competitionId = competitionId,
        competitionName = competitionName,
        competitionImageUrl = competitionImageUrl
    )
}

fun CompetitionData.toCompetitionDataDto(): CompetitionDataDto {
    return CompetitionDataDto(
            competitionId = competitionId,
            competitionName = competitionName,
            competitionImageUrl = competitionImageUrl
        )
    }
