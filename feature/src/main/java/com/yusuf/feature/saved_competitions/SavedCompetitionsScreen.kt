package com.yusuf.feature.saved_competitions

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yusuf.component.LoadingLottie
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.feature.R
import com.yusuf.feature.saved_competitions.state.GetSavedCompetitionsUIState
import com.yusuf.navigation.NavigationGraph
import com.yusuf.theme.DARK_BLUE
import com.yusuf.theme.GREEN
import com.yusuf.theme.LIGHT_GREEN

@Composable
fun SavedCompetitionsScreen (
    navController: NavController,
    viewModel: SavedCompetitionsViewModel = hiltViewModel()
){
    val state by viewModel.getSavedCompetitionsUIState.collectAsState(GetSavedCompetitionsUIState())

    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when {
                    state.isLoading -> {
                        LoadingLottie(resId = R.raw.loading_anim)
                    }

                    state.error != null -> {
                        Text(text = state.error ?: "An error occurred")
                        Log.d("SavedCompetitionsScreen", "SavedCompetitionsScreen: ${state.error}")
                    }
                    // if the list is empty firebase will return empty list
                    state.savedCompetitions!!.isEmpty() -> {
                        Text(text = "No saved competitions")
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .padding(
                                    bottom = paddingValues.calculateBottomPadding(),
                                    top = 70.dp
                                ),
                        ) {
                            items(state.savedCompetitions ?: emptyList()) { competition ->
                                ExpandableCard(
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
    )
}

@Composable
fun ExpandableCard(
    competition: SavedCompetitionsModel,
    onDeleteClick: (SavedCompetitionsModel) -> Unit,
    padding: Dp = 12.dp,
    navController: NavController
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = "rotation"
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            expandedState = !expandedState
        },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                AsyncImage(
                    model = "",
                    contentDescription = "Competition Image",
                    modifier = Modifier.size(100.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(start = 20.dp)
                ) {
                    Text(
                        text = competition.competitionTime,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = competition.competitionDate,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = GREEN,
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .alpha(0.2f)
                        .rotate(rotationState),
                        onClick = {
                            expandedState = !expandedState
                        }) {
                    Icon(
                        tint = Color.Red,
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop Down Arrow"
                    )
                }
            }
            if (expandedState) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CompetitionCard(
                        competition = competition,
                        onDelete = onDeleteClick
                    )

                    Button(colors = ButtonDefaults.buttonColors(
                        contentColor = DARK_BLUE
                    ), onClick = {
                            navController.navigate("${NavigationGraph.COMPETITION_DETAIL.route}/${competition.competitionId}")
                        }) {
                        Text(text = "Show All Details")
                    }
                }
            }
        }
    }
}

@Composable
fun CompetitionCard(
    competition: SavedCompetitionsModel,
    onDelete: (SavedCompetitionsModel) -> Unit
) {
    var shouldShowItemDeletionDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(corner = CornerSize(16.dp)),
        border = BorderStroke(2.dp, Color.Transparent)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = competition.competitionDate,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Justify,
                    fontSize = 13.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f)
                )
                Icon(
                    tint = Color.Black,
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Bottom)
                        .clickable {
                            shouldShowItemDeletionDialog = true
                        }
                )
                if (shouldShowItemDeletionDialog) {
                    CompetitionItemDeletionDialog({
                        shouldShowItemDeletionDialog = it
                    }, {
                        onDelete(competition)
                    })
                }
            }
        }
    }
}


@Composable
fun CompetitionItemDeletionDialog(
    onDismiss: (Boolean) -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        title = { Text(text = "Confirm Deletion") },
        text = { Text("Are you sure you want to delete this item?") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss(false)
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss(false) }
            ) {
                Text("No")
            }
        }
    )
}






