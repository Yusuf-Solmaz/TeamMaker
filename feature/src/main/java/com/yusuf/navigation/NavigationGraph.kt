package com.yusuf.navigation

enum class NavigationGraph(val route: String) {

        ONBOARDING_SCREEN("onboarding_screen"),
        CHOOSE_SPORT("choose_sport"),
        OPTIONS("options"),
        PLAYER_LIST("player_list"),
        CREATE_MATCH("create_match"),
        ADD_PLAYER("add_player"),
        LOGIN("login"),
        REGISTER("register"),
        FORGOT_PASSWORD("forgot_password");

        companion object {
            fun fromRoute(route: String?): NavigationGraph =
                entries.firstOrNull { it.route == route } ?: throw IllegalArgumentException("Invalid route: $route")
        }

}