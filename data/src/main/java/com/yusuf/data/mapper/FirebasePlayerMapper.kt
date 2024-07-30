package com.yusuf.data.mapper

import com.yusuf.data.remote.dto.firebase_dto.PlayerDataDto
import com.yusuf.domain.model.firebase.PlayerData

fun PlayerDataDto.toPlayerData(): PlayerData {
    return PlayerData(
        id = id,
        profilePhotoUrl = profilePhotoUrl,
        firstName = firstName,
        lastName = lastName,
        position = position,
        skillRating = skillRating
    )
}

fun PlayerData.toPlayerDataDto(): PlayerDataDto {
    return PlayerDataDto(
        id = id,
        profilePhotoUrl = profilePhotoUrl,
        firstName = firstName,
        lastName = lastName,
        position = position,
        skillRating = skillRating
    )
}