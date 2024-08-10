package com.yusuf.feature.saved_competitions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yusuf.component.LoadingLottie
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.feature.R
import com.yusuf.feature.saved_competitions.state.GetSavedCompetitionsUIState
import com.yusuf.navigation.NavigationGraph
import com.yusuf.theme.DARK_BLUE
import com.yusuf.theme.DARK_RED
import com.yusuf.theme.LIGHT_GREEN
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


@Composable
fun CompetitionCard(
    competition: SavedCompetitionsModel,
    onDeleteClick: (SavedCompetitionsModel) -> Unit,
    navController: NavController,
    padding: Dp = 8.dp,
) {
    var shouldShowItemDeletionDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth()
            .clickable {
                val route = NavigationGraph.getSavedCompetitionDetailsRoute(
                    SavedCompetitionsModel(
                        competitionId = competition.competitionId,
                        firstTeam = competition.firstTeam,
                        secondTeam = competition.secondTeam,
                        imageUrl = competition.imageUrl,
                        competitionTime = competition.competitionTime,
                        competitionDate = competition.competitionDate,
                        locationName = competition.locationName,
                        weatherModel = competition.weatherModel
                    )
                )
                navController.navigate(route)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(padding),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = competition.imageUrl,
                contentDescription = "Competition Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(padding))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = competition.competitionTime,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = competition.competitionDate,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    tint = Color.Black,
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            shouldShowItemDeletionDialog = true
                        }
                )
                if (shouldShowItemDeletionDialog) {
                    ShowConfirmationDialog({
                        shouldShowItemDeletionDialog = it
                    }, {
                        onDeleteClick(competition)
                    })
                }
            }
        }
    }
}

@Composable
fun ShowConfirmationDialog(
    onDismiss: (Boolean) -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        title = { Text(text = "Confirm Deletion") },
        text = { Text("Are you sure you want to delete this item?") },
        confirmButton = {
            AuthButtonComponent(
                value = "Yes",
                onClick = {
                    onConfirm()
                    onDismiss(false)
                },
                modifier = Modifier.width(60.dp),
                fillMaxWidth = false,
                heightIn = 35.dp,
                firstColor = RED,
                secondColor = DARK_RED
            )
        },
        dismissButton = {
            AuthButtonComponent(
                value = "No",
                onClick = {
                    onDismiss(false)
                },
                modifier = Modifier.width(60.dp),
                fillMaxWidth = false,
                heightIn = 35.dp
            )
        },
        modifier = Modifier,
        containerColor = Color.White
    )
}






