package com.yusuf.domain.model.competition_detail

import android.location.Location
import android.widget.TimePicker
import com.yusuf.domain.model.firebase.PlayerData
import java.io.Serializable

data class CompetitionDetail(
    val selectedTime: String,
    val selectedDate: String,
    val firstBalancedTeam: List<PlayerData>? = null,
    val secondBalancedTeam: List<PlayerData>? = null,
    val location: Location? = null,
    val locationName: String? = null
) : Serializable
