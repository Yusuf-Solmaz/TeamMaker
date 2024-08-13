package com.yusuf.feature.player_list.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.use_cases.firebase_use_cases.image.UpdatePlayerImageUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.AddPlayerUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.DeletePlayerByIdUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.GetPlayersByCompetitionTypeUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.UpdatePlayerByIdUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.player_list.state.AddPlayerUIState
import com.yusuf.feature.player_list.state.PlayerUiState
import com.yusuf.navigation.main_datastore.MainDataStore
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
    private val updatePlayerImageUseCase: UpdatePlayerImageUseCase,
    private val getPlayersByCompetitionTypeUseCase: GetPlayersByCompetitionTypeUseCase,
    private val mainDataStore: MainDataStore
) : ViewModel() {

    private val _playerUiState = MutableStateFlow(AddPlayerUIState())
    val playerUiState: StateFlow<AddPlayerUIState> = _playerUiState

    private val _playerListUIState = MutableStateFlow(PlayerUiState())
    val playerListUIState: StateFlow<PlayerUiState> = _playerListUIState

    val showTooltip = mainDataStore.readShowTooltip.asLiveData()

    internal fun saveShowTooltip(show: Boolean) {
        viewModelScope.launch {
            mainDataStore.saveShowTooltip(show)
        }
    }

    internal fun getPlayersByCompetitionType(competitionType: String) {
        _playerListUIState.value = _playerListUIState.value.copy(isLoading = true)
        viewModelScope.launch {
            getPlayersByCompetitionTypeUseCase(competitionType).collect { result ->
                when (result) {
                    is RootResult.Loading -> {
                        _playerListUIState.value = _playerListUIState.value.copy(
                            isLoading = true,
                            error = null,
                            playerList = emptyList()
                        )
                    }

                    is RootResult.Error -> {
                        _playerListUIState.value = _playerListUIState.value.copy(
                            error = result.message,
                            isLoading = false,
                            playerList = emptyList()
                        )
                    }

                    is RootResult.Success -> {
                        _playerListUIState.value = _playerListUIState.value.copy(
                            playerList = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
        }
    }


    internal fun updatePlayerImage(
        uri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        _playerUiState.value = _playerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            updatePlayerImageUseCase(uri, onSuccess, onFailure).collect { result ->
                when (result) {
                    is RootResult.Loading -> {
                        _playerUiState.value = _playerUiState.value.copy(
                            isLoading = true,
                            error = null,
                            transaction = false
                        )
                    }

                    is RootResult.Error -> {
                        _playerUiState.value = _playerUiState.value.copy(
                            error = result.message,
                            isLoading = false,
                            transaction = false
                        )
                    }

                    is RootResult.Success -> {
                        _playerUiState.value = _playerUiState.value.copy(
                            transaction = true,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            }
        }
    }

    internal fun deletePlayerById(playerId: String, competitionType: String) {
        _playerUiState.value = _playerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            deletePlayerByIdUseCase(playerId).collect { result ->
                when (result) {
                    is RootResult.Loading -> {
                        _playerUiState.value = _playerUiState.value.copy(
                            isLoading = true,
                            error = null,
                            transaction = false
                        )
                    }

                    is RootResult.Error -> {
                        _playerUiState.value = _playerUiState.value.copy(
                            error = result.message,
                            isLoading = false,
                            transaction = false
                        )
                    }

                    is RootResult.Success -> {
                        _playerUiState.value = _playerUiState.value.copy(
                            transaction = true,
                            isLoading = false,
                            error = null
                        )
                        getPlayersByCompetitionType(competitionType)
                    }
                }
            }
        }
    }

    internal fun updatePlayerById(playerId: String, updatedPlayerData: PlayerData) {
        _playerUiState.value = _playerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            updatePlayerByIdUseCase(playerId, updatedPlayerData).collect { result ->
                when (result) {
                    is RootResult.Loading -> {
                        Log.d("AddPlayerViewModel", "Loading")
                        _playerUiState.value = _playerUiState.value.copy(
                            isLoading = true,
                            error = null,
                            transaction = false
                        )
                    }

                    is RootResult.Error -> {
                        Log.d("AddPlayerViewModel", "Error: ${result.message}")
                        _playerUiState.value = _playerUiState.value.copy(
                            error = result.message,
                            isLoading = false,
                            transaction = false
                        )
                    }

                    is RootResult.Success -> {
                        Log.d("PlayerListViewModel", "Success")
                        _playerUiState.value = _playerUiState.value.copy(
                            transaction = true,
                            isLoading = false,
                            error = null
                        )
                        getPlayersByCompetitionType(updatedPlayerData.competitionType)
                    }
                }
            }
        }
    }

    internal fun addPlayer(playerData: PlayerData, imageUri: Uri) {
        _playerUiState.value = _playerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            addPlayerUseCase(playerData, imageUri, "profile_images").collect { result ->
                when (result) {
                    is RootResult.Loading -> {
                        Log.d("AddPlayerViewModel", "Loading")
                        _playerUiState.value = _playerUiState.value.copy(
                            isLoading = true,
                            error = null,
                            transaction = false
                        )
                    }

                    is RootResult.Error -> {
                        Log.d("AddPlayerViewModel", "Error: ${result.message}")
                        _playerUiState.value = _playerUiState.value.copy(
                            error = result.message,
                            isLoading = false,
                            transaction = false
                        )
                    }

                    is RootResult.Success -> {
                        Log.d("AddPlayerViewModel", "Success")
                        _playerUiState.value = _playerUiState.value.copy(
                            transaction = true,
                            isLoading = false,
                            error = null
                        )
                        getPlayersByCompetitionType(playerData.competitionType)
                    }
                }
            }
        }
    }
}