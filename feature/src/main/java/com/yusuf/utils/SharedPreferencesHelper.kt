package com.yusuf.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    var competitionName: String?
        get() = sharedPreferences.getString("competitionName", null)
        set(value) = sharedPreferences.edit().putString("competitionName", value).apply()
}
