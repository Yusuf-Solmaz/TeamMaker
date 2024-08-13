package com.yusuf.utils.default_competition

import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.feature.R
import java.io.Serializable

data class Competition(
        val competitionName: String,
        val competitionFirstImage: Int,
        val competitionTeamImage: Int
): Serializable

val predefinedCompetitions = listOf(
        Competition("Football", R.drawable.football_1, R.drawable.football_team_2),
        Competition("Basketball", R.drawable.basketball_1, R.drawable.basketball_team_2),
        Competition("Volleyball", R.drawable.volleyball_1, R.drawable.volleyball_team_2),
        Competition("Taboo", R.drawable.taboo_1, R.drawable.taboo_team),
        Competition("Codenames", R.drawable.codenames_1, R.drawable.codenames_team),
        Competition("Pictionary", R.drawable.pictionary_1, R.drawable.pictionary_team),
        Competition("Handball", R.drawable.handball_1, R.drawable.handball_team),
        Competition("Dodgeball", R.drawable.dodgeball_1, R.drawable.dodgeball_team)
)

fun CompetitionData.toCompetition(): Competition {
        return predefinedCompetitions.find { it.competitionName == this.competitionName }?.let {
                Competition(
                        competitionName = it.competitionName,
                        competitionFirstImage = it.competitionFirstImage,
                        competitionTeamImage = it.competitionTeamImage
                )
        } ?: Competition(
                competitionName = this.competitionName,
                competitionFirstImage = 0,
                competitionTeamImage = 0
        )
}


