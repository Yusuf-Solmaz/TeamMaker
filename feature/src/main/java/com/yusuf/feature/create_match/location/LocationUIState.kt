package com.yusuf.feature.create_match.location

import android.location.Location

data class LocationUIState(
    val location: Location? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
