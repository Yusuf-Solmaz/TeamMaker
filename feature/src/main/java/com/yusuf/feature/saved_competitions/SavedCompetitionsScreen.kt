package com.yusuf.feature.saved_competitions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yusuf.component.LoadingLottie
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.feature.R
import com.yusuf.feature.saved_competitions.state.GetSavedCompetitionsUIState
import com.yusuf.navigation.NavigationGraph
import com.yusuf.theme.DARK_RED
import com.yusuf.theme.RED

@Composable
fun SavedCompetitionsScreen(
    navController: NavController,
    viewModel: SavedCompetitionsViewModel = hiltViewModel()
) {
    val state by viewModel.getSavedCompetitionsUIState.collectAsState(GetSavedCompetitionsUIState())


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            state.isLoading -> {
                LoadingLottie(resId = R.raw.loading_anim)
            }

            state.error != null -> {
                Text(text = state.error ?: "An error occurred")
            }
            // if the list is empty firebase will return empty list
            state.savedCompetitions!!.isEmpty() -> {
                Text(text = "No saved competitions")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                ) {
                    items(state.savedCompetitions ?: emptyList()) { competition ->
                        CompetitionCard(
                            competition = competition,
                            onDeleteClick = {
                                viewModel.deleteSavedCompetition(competition.competitionId)
                            },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}





