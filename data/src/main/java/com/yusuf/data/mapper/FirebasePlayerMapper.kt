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
        competitionType = competitionType,
        focus = focus,
        speed = speed,
        condition = condition,
        durability = durability,
        generalSkill = generalSkill,
        totalSkillRating = totalSkillRating
    )
}

fun PlayerData.toPlayerDataDto(): PlayerDataDto {
    return PlayerDataDto(
        id = id,
        profilePhotoUrl = profilePhotoUrl,
        firstName = firstName,
        lastName = lastName,
        position = position,
        competitionType = competitionType,
        focus = focus,
        speed = speed,
        condition = condition,
        durability = durability,
        generalSkill = generalSkill,
        totalSkillRating = totalSkillRating
    )
}