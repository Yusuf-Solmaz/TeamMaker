package com.yusuf.feature.player_list.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.use_cases.firebase_use_cases.user.AddPlayerUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.DeletePlayerByIdUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.GetAllPlayersUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.UpdatePlayerByIdUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.player_list.state.AddPlayerUIState
import com.yusuf.feature.player_list.state.PlayerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerListViewModel @Inject constructor(
    private val addPlayerUseCase: AddPlayerUseCase,
    private val deletePlayerByIdUseCase: DeletePlayerByIdUseCase,
    private val updatePlayerByIdUseCase: UpdatePlayerByIdUseCase,
    private val getAllPlayersUseCase: GetAllPlayersUseCase
) : ViewModel(){

    private val _playerUiState = MutableStateFlow(AddPlayerUIState())
    val playerUiState: StateFlow<AddPlayerUIState> = _playerUiState

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

    fun deletePlayerById(playerId: String){
        _playerUiState.value = _playerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            deletePlayerByIdUseCase(playerId).collect{
                result->
                when(result){
                    is RootResult.Loading -> {
                        Log.d("AddPlayerViewModel", "Loading")
                        _playerUiState.value = _playerUiState.value.copy(isLoading = true,error = null,transaction = false)
                    }
                    is RootResult.Error -> {
                        Log.d("AddPlayerViewModel", "Error: ${result.message}")
                        _playerUiState.value = _playerUiState.value.copy(error = result.message, isLoading = false,transaction = false)
                    }
                    is RootResult.Success -> {
                        Log.d("AddPlayerViewModel", "Success")
                        _playerUiState.value = _playerUiState.value.copy(transaction = true, isLoading = false, error = null)
                        getAllPlayers()
                    }
                }
            }
        }
    }

    fun updatePlayerById(playerId: String, updatedPlayerData: PlayerData){
        _playerUiState.value = _playerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            updatePlayerByIdUseCase(playerId, updatedPlayerData).collect{
                result->
                when(result){
                    is RootResult.Loading -> {
                        Log.d("AddPlayerViewModel", "Loading")
                        _playerUiState.value = _playerUiState.value.copy(isLoading = true,error = null,transaction = false)
                    }
                    is RootResult.Error -> {
                        Log.d("AddPlayerViewModel", "Error: ${result.message}")
                        _playerUiState.value = _playerUiState.value.copy(error = result.message, isLoading = false,transaction = false)
                    }
                    is RootResult.Success -> {
                        Log.d("AddPlayerViewModel", "Success")
                        _playerUiState.value = _playerUiState.value.copy(transaction = true, isLoading = false, error = null)
                        getAllPlayers()
                    }
                }
            }
        }
    }

    fun addPlayer(playerData: PlayerData){
        _playerUiState.value = _playerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            addPlayerUseCase(playerData).collect{
                result->
                when(result){
                    is RootResult.Loading -> {
                        Log.d("AddPlayerViewModel", "Loading")
                        _playerUiState.value = _playerUiState.value.copy(isLoading = true,error = null,transaction = false)
                    }
                    is RootResult.Error -> {
                        Log.d("AddPlayerViewModel", "Error: ${result.message}")
                        _playerUiState.value = _playerUiState.value.copy(error = result.message, isLoading = false,transaction = false)
                    }
                    is RootResult.Success -> {
                        Log.d("AddPlayerViewModel", "Success")
                        _playerUiState.value = _playerUiState.value.copy(transaction = true, isLoading = false, error = null)
                        getAllPlayers()
                    }
                }
            }
        }
    }
}