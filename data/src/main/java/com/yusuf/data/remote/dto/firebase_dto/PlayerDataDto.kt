package com.yusuf.data.remote.dto.firebase_dto

import java.io.Serializable
import java.util.UUID

data class PlayerDataDto(
    val id: String = UUID.randomUUID().toString(),
    val profilePhotoUrl: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val position: String = "",
    val competitionType:String= "",
    val focus: Int= 0,
    val speed: Int= 0,
    val condition: Int= 0,
    val durability: Int= 0,
    val generalSkill: Int=0,
    val totalSkillRating: Int= 0,
): Serializable

