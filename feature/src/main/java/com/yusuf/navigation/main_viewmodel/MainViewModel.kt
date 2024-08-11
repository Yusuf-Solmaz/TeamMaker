package com.yusuf.navigation.main_viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.navigation.NavigationGraph
import com.yusuf.navigation.main_datastore.MainDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myPreferencesDataStore: MainDataStore
): ViewModel() {

    var isLoading by mutableStateOf(true)
        private set

    var startDestination by mutableStateOf("onboarding_screen")
        private set

    var isSplashScreenVisible by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {

            delay(2700)
            isSplashScreenVisible = false

            myPreferencesDataStore.readAppEntry.collect { loadOnBoardingScreen ->
                startDestination = if (loadOnBoardingScreen) {
                    NavigationGraph.ONBOARDING_SCREEN.route
                } else {
                    NavigationGraph.IS_LOGGED_IN.route
                }
                delay(2500)
                isLoading = false
            }
        }
    }

    fun saveAppEntry() {
        viewModelScope.launch {
            myPreferencesDataStore.saveAppEntry()
        }
    }
}