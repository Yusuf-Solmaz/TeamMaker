package com.yusuf.utils

import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.feature.R
import java.io.Serializable

fun CompetitionData.toCompetitionList(): CompetitionList? {
        return CompetitionList.values().find { it.competitionName == this.competitionName }
}

enum class CompetitionList(val competitionName: String,val competitionFirstImage:Int,val competitionTeamImage:Int): Serializable  {
        FOOTBALL("Football", R.drawable.football_1,R.drawable.football_team_2),
        BASKETBALL("Basketball", R.drawable.basketball_1,R.drawable.basketball_team_2),
        DEFAULT("Default", R.drawable.players, R.drawable.players); // Add this line

}