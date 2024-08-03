package com.yusuf.navigation

import com.google.gson.Gson
import com.yusuf.domain.model.competition_detail.CompetitionDetail
import com.yusuf.utils.Competition

enum class NavigationGraph(val route: String) {
        ONBOARDING_SCREEN("onboarding_screen"),
        CHOOSE_SPORT("choose_sport"),
        OPTIONS("options/{competitionJson}"),
        PLAYER_LIST("player_list"),
        CREATE_MATCH("create_match"),
        MATCH_DETAIL("match_detail/{competitionDetailJson}"),
        LOGIN("login"),
        REGISTER("register"),
        FORGOT_PASSWORD("forgot_password");

        companion object {
                fun getOptionsRoute(competition: Competition): String {
                        val gson = Gson()
                        val competitionJson = gson.toJson(competition)
                        return "options/$competitionJson"
                }
                fun getCompetitionDetailsRoute(competitionDetail: CompetitionDetail): String {
                        val gson = Gson()
                        val competitionDetailJson = gson.toJson(competitionDetail)
                        return "match_detail/$competitionDetailJson"
                }
        }
}