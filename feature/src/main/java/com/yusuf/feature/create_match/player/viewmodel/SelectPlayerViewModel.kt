package com.yusuf.feature.create_match.player.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.use_cases.firebase_use_cases.user.GetAllPlayersUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.create_match.player.state.PlayerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectPlayerViewModel @Inject constructor(
    private val getAllPlayersUseCase: GetAllPlayersUseCase
): ViewModel() {

    private val _playerListUIState = MutableStateFlow(PlayerUiState())
    val playerListUIState: StateFlow<PlayerUiState> = _playerListUIState


    fun getAllPlayers(){
        _playerListUIState.value = _playerListUIState.value.copy(isLoading = true)
        viewModelScope.launch {
            getAllPlayersUseCase().collect{
                    result->
                when(result){
                    is RootResult.Loading -> {
                        Log.d("AddPlayerViewModel", "Loading")
                        _playerListUIState.value = _playerListUIState.value.copy(isLoading = true,error = null,playerList = emptyList())
                    }
                    is RootResult.Error -> {
                        Log.d("AddPlayerViewModel", "Error: ${result.message}")
                        _playerListUIState.value = _playerListUIState.value.copy(error = result.message, isLoading = false,playerList = emptyList())
                    }
                    is RootResult.Success -> {
                        Log.d("AddPlayerViewModel", "Success")
                        _playerListUIState.value = _playerListUIState.value.copy(playerList = result.data, isLoading = false, error = null)
                    }
                }
            }
        }
    }
}