package com.yusuf.data.remote.dto.firebase_dto

import java.io.Serializable
import java.util.UUID

data class CompetitionDataDto(
    val competitionId: String = UUID.randomUUID().toString(),
    val competitionName: String="",
    val competitionImageUrl: String=""
):Serializable