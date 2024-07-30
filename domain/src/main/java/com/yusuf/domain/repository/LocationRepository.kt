package com.yusuf.domain.repository

import android.location.Location

interface LocationRepository {
    suspend fun getLocation(): Location?
}