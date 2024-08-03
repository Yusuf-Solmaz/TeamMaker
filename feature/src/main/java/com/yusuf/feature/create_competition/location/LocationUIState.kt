package com.yusuf.feature.create_competition.location

import android.location.Location

data class LocationUIState(
    val location: Location? = null,
    val locationName: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
