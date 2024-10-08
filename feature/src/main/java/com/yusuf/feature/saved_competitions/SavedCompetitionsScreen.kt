package com.yusuf.feature.saved_competitions

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yusuf.component.LoadingLottie
import com.yusuf.feature.R
import com.yusuf.feature.saved_competitions.state.GetSavedCompetitionsUIState
import com.yusuf.feature.saved_competitions.viewmodel.SavedCompetitionsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

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
                Text(
                    text = "You don't have any saved competitions yet.",
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontFamily = FontFamily(Font(R.font.onboarding_title1, FontWeight.Normal)),
                    style = TextStyle(fontSize = 20.sp)
                )
            }

            else -> {
                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

                val sortedCompetitions = state.savedCompetitions?.sortedBy { competition ->
                    dateFormat.parse(competition.competitionDate)}

                Log.d("SavedCompetitionsScreen", "Saved Competitions: ${state.savedCompetitions!![0].competitionDate}")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                ) {
                    items(sortedCompetitions ?: emptyList()) { competition ->
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






