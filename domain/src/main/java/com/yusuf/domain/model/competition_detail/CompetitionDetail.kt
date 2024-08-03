package com.yusuf.domain.model.competition_detail

import android.widget.TimePicker
import com.yusuf.domain.model.firebase.PlayerData

data class CompetitionDetail(
    val selectedTime: String,
    //val balancedTeam: Pair<List<PlayerData>, List<PlayerData>>? = null
)
