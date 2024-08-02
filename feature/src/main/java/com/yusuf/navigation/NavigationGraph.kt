package com.yusuf.navigation

import com.google.gson.Gson
import com.yusuf.utils.Competition

enum class NavigationGraph(val route: String) {
        ONBOARDING_SCREEN("onboarding_screen"),
        CHOOSE_SPORT("choose_sport"),
        OPTIONS("options/{competitionJson}"),
        PLAYER_LIST("player_list"),
        CREATE_MATCH("create_match"),
        MATCH_DETAIL("match_detail"),
        LOGIN("login"),
        REGISTER("register"),
        FORGOT_PASSWORD("forgot_password");

        companion object {
                fun getOptionsRoute(competition: Competition): String {
                        val gson = Gson()
                        val competitionJson = gson.toJson(competition)
                        return "options/$competitionJson"
                }
        }
}