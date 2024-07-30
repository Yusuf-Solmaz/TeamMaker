package com.yusuf.feature.home.state


import com.yusuf.domain.util.RootResult

data class AddDeleteState(
    val isLoading: Boolean = false,
    val result: RootResult<Boolean>? = null,
    val error: String? = null
)

