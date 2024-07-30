package com.yusuf.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.use_cases.firebase_use_cases.user.GetAllCompetitionsUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.home.state.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAllCompetitionsViewModel @Inject constructor(
    private val getAllCompetitionsUseCase: GetAllCompetitionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState: StateFlow<HomeScreenState> = _uiState

    fun getAllCompetitions() {
        _uiState.value = _uiState.value.copy(isLoading = true, getAllCompetitionsResult = RootResult.Loading)
        viewModelScope.launch {
            getAllCompetitionsUseCase().collect { result ->
                _uiState.value = _uiState.value.copy(
                    isLoading = result is RootResult.Loading,
                    getAllCompetitionsResult = result
                )
            }
        }
    }
}
