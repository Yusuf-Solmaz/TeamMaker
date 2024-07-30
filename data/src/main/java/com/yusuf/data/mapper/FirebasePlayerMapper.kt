package com.yusuf.data.mapper

import com.yusuf.data.remote.dto.firebase_dto.PlayerDataDto
import com.yusuf.domain.model.firebase.PlayerData

fun PlayerDataDto.toPlayerData(): PlayerData {
    return PlayerData(
        profilePhotoUrl = profilePhotoUrl,
        firstName = firstName,
        lastName = lastName,
        position = position,
        skillRating = skillRating
    )
}

fun PlayerData.toPlayerDataDto(): PlayerDataDto {
    return PlayerDataDto(
        profilePhotoUrl = profilePhotoUrl,
        firstName = firstName,
        lastName = lastName,
        position = position,
        skillRating = skillRating
    )
}