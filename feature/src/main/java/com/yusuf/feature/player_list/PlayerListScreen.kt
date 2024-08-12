package com.yusuf.feature.player_list

import android.annotation.SuppressLint
import android.net.Uri
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import coil.compose.rememberAsyncImagePainter
import com.yusuf.component.LoadingLottie
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import com.yusuf.component.custom_player_dialog.AddPlayerDialog
import com.yusuf.component.custom_player_dialog.UpdatePlayerDialog
import com.yusuf.component.custom_player_dialog.showTooltipBalloon
import com.yusuf.feature.player_list.viewmodel.PlayerListViewModel
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.utils.SharedPreferencesHelper
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayerListScreen(
    viewModel: PlayerListViewModel = hiltViewModel()
) {
    val playerListUiState by viewModel.playerListUIState.collectAsState()
    val playerTransactionUiState by viewModel.playerUiState.collectAsState()
    val showTooltip by viewModel.showTooltip.observeAsState(true)

    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var showUpdatePlayerDialog by remember { mutableStateOf(false) }
    var playerDataToUpdate by remember { mutableStateOf<PlayerData?>(null) }

    val context = LocalContext.current
    val sharedPreferencesHelper = remember { SharedPreferencesHelper(context) }
    val competitionName = sharedPreferencesHelper.competitionName

    LaunchedEffect(true) {
        viewModel.getPlayersByCompetitionType(competitionName.toString())
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = APPBAR_GREEN, contentColor = Color.White,
                onClick = {
                showAddPlayerDialog = true
                if (showTooltip) {
                    viewModel.saveShowTooltip(false)
                }
            }) {
                Text(text = "Add Player")
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when {
                    playerListUiState.isLoading || playerTransactionUiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingLottie(R.raw.loading_anim)
                        }
                    }
                    playerListUiState.error != null -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Error: ${playerListUiState.error}")
                        }
                    }
                    playerListUiState.playerList != null -> {
                        if (playerListUiState.playerList?.isEmpty() == true) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "You don't have any players yet.",
                                    modifier = Modifier.padding(bottom = 10.dp),
                                    fontFamily = FontFamily(Font(R.font.onboarding_title1, FontWeight.Normal)),
                                    style = TextStyle(fontSize = 20.sp)
                                )
                                Text(
                                    text = "Start adding now.",
                                    fontFamily = FontFamily(Font(R.font.onboarding_title1, FontWeight.Normal)),
                                    style = TextStyle(fontSize = 20.sp)
                                )
                            }
                        }

                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(playerListUiState.playerList!!.size) { index ->
                                PlayerListItem(
                                    playerData = playerListUiState.playerList!![index],
                                    onDelete = { id ->
                                        viewModel.deletePlayerById(id, competitionName.toString())
                                        viewModel.getPlayersByCompetitionType(competitionName.toString())
                                    },
                                    onUpdatePlayer = { playerData ->
                                        playerDataToUpdate = playerData
                                        showUpdatePlayerDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }

            if (showAddPlayerDialog) {
                AddPlayerDialog(
                    competitionName = competitionName.toString(),
                    onDismiss = { showAddPlayerDialog = false },
                    onAddPlayer = { playerData ->
                        viewModel.addPlayer(
                            PlayerData(
                                profilePhotoUrl = playerData.toString(),
                                firstName = playerData.firstName,
                                lastName = playerData.lastName,
                                position = playerData.position,
                                competitionType = playerData.competitionType,
                                speed = playerData.speed,
                                focus = playerData.focus,
                                condition = playerData.condition,
                                durability = playerData.durability,
                                generalSkill = playerData.generalSkill,
                                totalSkillRating = playerData.totalSkillRating
                            ),
                            Uri.parse(playerData.profilePhotoUrl)
                        )
                        viewModel.getPlayersByCompetitionType(competitionName.toString())
                        showAddPlayerDialog = false
                    },
                    updateList = { },
                    context = context
                )
            }

            if (showUpdatePlayerDialog && playerDataToUpdate != null) {
                UpdatePlayerDialog(
                    playerData = playerDataToUpdate!!,
                    onDismiss = { showUpdatePlayerDialog = false },
                    onUpdatePlayer = { updatedPlayerData ->
                        viewModel.updatePlayerById(updatedPlayerData.id, updatedPlayerData)
                        showUpdatePlayerDialog = false
                    },
                    viewModel = viewModel,
                    context = context
                )
            }
        }
    )

    if (showTooltip) {
        AndroidView(
            factory = { context ->
                View(context).apply {
                    post {
                        showTooltipBalloon(context, this, "You can slide right to edit or left to delete players") {
                            viewModel.saveShowTooltip(false)
                        }
                    }
                }
            }
        )
    }
}


@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun PlayerListItem(
    playerData: PlayerData,
    onDelete: (String) -> Unit,
    onUpdatePlayer: (PlayerData) -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { 100.dp.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1, -sizePx to -1)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Horizontal
            )
    ) {
        // Background for Edit button
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .align(Alignment.CenterStart)
        ) {
            if (swipeableState.currentValue == 1) {
                IconButton(
                    onClick = { onUpdatePlayer(playerData) },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(100.dp)
                .align(Alignment.CenterEnd)
        ) {
            if (swipeableState.currentValue == -1) {
                IconButton(
                    onClick = { onDelete(playerData.id) },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                       .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Card content
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
                .zIndex(1f),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = rememberAsyncImagePainter(playerData.profilePhotoUrl),
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${playerData.firstName} ${playerData.lastName}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Player Position:${playerData.position}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Total Skill Rating: ${playerData.totalSkillRating}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}





