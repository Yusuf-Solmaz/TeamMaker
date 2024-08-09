package com.yusuf.navigation

import android.net.Uri
import com.google.gson.Gson
import com.yusuf.domain.model.competition_detail.CompetitionDetail
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.utils.default_competition.Competition

enum class NavigationGraph(val route: String) {
        ONBOARDING_SCREEN("onboarding_screen"),
        CHOOSE_COMPETITION_TYPE("choose_competition_type"),
        OPTIONS("options/{competitionJson}"),
        PLAYER_LIST("player_list"),
        CREATE_COMPETITION("create_competition"),
        COMPETITION_DETAIL("competition_detail/{competitionDetailJson}"),
        SAVED_COMPETITION_DETAIL("saved_competition_detail/{savedCompetitionDetailJson}"),
        LOGIN("login"),
        REGISTER("register"),
        FORGOT_PASSWORD("forgot_password"),
        SAVED_COMPETITIONS("saved_competitions");

        companion object {
                fun getOptionsRoute(competition: Competition): String {
                        val gson = Gson()
                        val competitionJson = gson.toJson(competition)
                        return "options/$competitionJson"
                }
                fun getCompetitionDetailsRoute(competitionDetail: CompetitionDetail): String {
                        val gson = Gson()
                        val competitionDetailJson = gson.toJson(competitionDetail)
                        return "competition_detail/${Uri.encode(competitionDetailJson)}"
                }

                fun getSavedCompetitionDetailsRoute(savedCompetitionsModel: SavedCompetitionsModel): String {
                        val gson = Gson()
                        val savedCompetitionDetailJson = gson.toJson(savedCompetitionsModel)
                        return "saved_competition_detail/${Uri.encode(savedCompetitionDetailJson)}"
                }
        }
}