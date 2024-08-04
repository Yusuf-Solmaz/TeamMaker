package com.yusuf.domain.model.firebase

import java.util.UUID

data class PlayerData(
    val id: String = UUID.randomUUID().toString(),
    val profilePhotoUrl: String,
    val firstName: String,
    val lastName: String,
    val position: String,
    val competitionType:String,
    val focus: Int,
    val speed: Int,
    val condition: Int,
    val durability: Int,
    val generalSkill: Int,
    val totalSkillRating: Int,
)