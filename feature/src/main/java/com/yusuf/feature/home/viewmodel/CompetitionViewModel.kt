package com.yusuf.feature.home.viewmodel


import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.use_cases.firebase_use_cases.image.ImageUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.AddCompetitionUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.DeleteCompetitionUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.GetAllCompetitionsUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.UpdateCompetitionUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.domain.util.RootResult.*
import com.yusuf.feature.home.state.AddDeleteState
import com.yusuf.feature.home.state.GetAllState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitionViewModel @Inject constructor(
    private val addCompetitionUseCase: AddCompetitionUseCase,
    private val deleteCompetitionUseCase: DeleteCompetitionUseCase,
    private val getAllCompetitionsUseCase: GetAllCompetitionsUseCase,
    private val uploadImageUseCase: ImageUseCase,
    private val updateCompetitionUseCase: UpdateCompetitionUseCase
) : ViewModel() {

    private val _addDeleteState = MutableStateFlow(AddDeleteState())
    val addDeleteState: StateFlow<AddDeleteState> = _addDeleteState

    private val _getAllState = MutableStateFlow(GetAllState())
    val getAllState: StateFlow<GetAllState> = _getAllState


    private fun addCompetition(competitionData: CompetitionData) {
        _addDeleteState.value = AddDeleteState(isLoading = true, result = Loading)
        viewModelScope.launch {
            addCompetitionUseCase(competitionData).collect { result ->
                _addDeleteState.value = AddDeleteState(
                    isLoading = result is Loading,
                    result = result,
                    error = if (result is Error) result.message else null
                )
                if (result is Success) {
                    getAllCompetitions()
                }
            }
        }
    }

    fun deleteCompetition(competitionData: CompetitionData) {
        _addDeleteState.value = AddDeleteState(isLoading = true, result = Loading)
        viewModelScope.launch {
            deleteCompetitionUseCase(competitionData).collect { result ->
                _addDeleteState.value = AddDeleteState(
                    isLoading = result is Loading,
                    result = result,
                    error = if (result is Error) result.message else null
                )
                if (result is Success) {
                    getAllCompetitions()
                }
            }
        }
    }

    fun getAllCompetitions() {
        _getAllState.value = GetAllState(isLoading = true, result = Loading)
        viewModelScope.launch {
            getAllCompetitionsUseCase().collect { result ->
                _getAllState.value = GetAllState(
                    isLoading = result is Loading,
                    competitions = if (result is Success) result.data
                        ?: emptyList() else emptyList(),
                    result = result,
                    error = if (result is Error) result.message else null
                )
            }
        }
    }

    fun uploadImageAndAddCompetition(bitmap: Bitmap, competitionName: String) {
        viewModelScope.launch {
            val result = uploadImageUseCase(bitmap, "competitions")
            if (result.isSuccess) {
                val imageUrl = result.getOrNull()
                val competition = CompetitionData(
                    competitionName = competitionName,
                    competitionImageUrl = imageUrl ?: ""
                )
                addCompetition(competition)
            } else {
                // Handle the error if needed
            }
        }
    }


    fun updateCompetition(competitionId: String, competitionData: CompetitionData, imageUri: Uri?, context: Context) {
        _addDeleteState.value = AddDeleteState(isLoading = true, result = Loading)
        viewModelScope.launch {
            var updatedCompetitionData = competitionData
            imageUri?.let { uri ->
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                val result = uploadImageUseCase(bitmap, competitionData.competitionName)
                if (result.isSuccess) {
                    val imageUrl = result.getOrNull() ?: ""
                    updatedCompetitionData = updatedCompetitionData.copy(competitionImageUrl = imageUrl)
                }
            }
            updateCompetitionUseCase(competitionId, updatedCompetitionData).collect { result ->
                _addDeleteState.value = AddDeleteState(
                    isLoading = result is Loading,
                    result = result,
                    error = if (result is Error) result.message else null
                )
                if (result is RootResult.Success) {
                    getAllCompetitions()
                }
            }
        }
    }
}
