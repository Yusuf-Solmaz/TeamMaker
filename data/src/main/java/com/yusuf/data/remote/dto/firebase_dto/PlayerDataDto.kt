package com.yusuf.data.remote.dto.firebase_dto

import java.io.Serializable
import java.util.UUID

data class PlayerDataDto(
    val id: String = UUID.randomUUID().toString(),
    val profilePhotoUrl: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val position: String = "",
    val skillRating: Int = 0
): Serializable

