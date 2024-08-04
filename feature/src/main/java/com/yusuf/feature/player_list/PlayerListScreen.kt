package com.yusuf.feature.player_list

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import coil.compose.rememberAsyncImagePainter
import com.yusuf.component.LoadingLottie
import com.yusuf.component.TextFieldComponent
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import com.yusuf.feature.player_list.di.MainDataStoreEntryPoint
import com.yusuf.feature.player_list.viewmodel.PlayerListViewModel
import com.yusuf.utils.SharedPreferencesHelper
import dagger.hilt.android.EntryPointAccessors.fromApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.math.roundToInt

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayerListScreen(
    viewModel: PlayerListViewModel = hiltViewModel(),
) {
    val playerListUiState by viewModel.playerListUIState.collectAsState()
    val context = LocalContext.current

    val mainDataStore = remember {
        fromApplication(context, MainDataStoreEntryPoint::class.java).mainDataStore
    }

    var showTooltip by remember { mutableStateOf(false) }
    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var showUpdatePlayerDialog by remember { mutableStateOf(false) }
    var playerDataToUpdate by remember { mutableStateOf<PlayerData?>(null) }
    val sharedPreferencesHelper = remember { SharedPreferencesHelper(context) }
    val competitionName = sharedPreferencesHelper.competitionName

    // Refresh player list when the screen is recomposed
    LaunchedEffect(Unit) {
        viewModel.getPlayersByCompetitionType(competitionName.toString())
    }

    // Tooltip handling
    LaunchedEffect(Unit) {
        val tooltipShown = mainDataStore.readTooltipShown.first()
        if (!tooltipShown) {
            showTooltip = true
        }
    }

    LaunchedEffect(showTooltip) {
        if (showTooltip) {
            delay(3000)
            showTooltip = false
            mainDataStore.saveTooltipShown()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddPlayerDialog = true }) {
                Text(text = "Add Player")
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (showTooltip) {
                    Tooltip(
                        text = "Swipe left or right to see more options.",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                            .offset(y = 100.dp)
                    )
                }

                when {
                    playerListUiState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingLottie(R.raw.loading_anim)
                        }
                    }

                    playerListUiState.error != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Error: ${playerListUiState.error}")
                        }
                    }

                    playerListUiState.playerList != null -> {
                        if (playerListUiState.playerList?.isEmpty() == true) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "You don't have any players yet.",
                                    modifier = Modifier.padding(bottom = 10.dp),
                                    fontFamily = FontFamily(
                                        Font(R.font.onboarding_title1, FontWeight.Normal)
                                    ),
                                    style = TextStyle(
                                        fontSize = 20.sp
                                    )
                                )
                                Text(
                                    text = "Start adding now.",
                                    fontFamily = FontFamily(
                                        Font(R.font.onboarding_title1, FontWeight.Normal)
                                    ),
                                    style = TextStyle(
                                        fontSize = 20.sp
                                    )
                                )
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
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
                                totalSkillRating = playerData.totalSkillRating
                            ),
                            Uri.parse(playerData.profilePhotoUrl)
                        )
                        viewModel.getPlayersByCompetitionType(competitionName.toString())
                        showAddPlayerDialog = false
                    },
                    updateList = {
                    }
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
                    viewModel = viewModel
                )
            }
        }
    )
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

        // Background for Delete button
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
                containerColor = MaterialTheme.colorScheme.surface
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
                        text = "Player Name:${playerData.firstName} ${playerData.lastName}",
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
@Composable
fun Tooltip(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp))
            .shadow(8.dp, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}





@Composable
fun AddPlayerDialog(
    competitionName: String,
    onDismiss: () -> Unit,
    onAddPlayer: (playerData: PlayerData) -> Unit,
    updateList: () -> Unit
) {
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var speed by remember { mutableStateOf(0) }
    var focus by remember { mutableStateOf(0) }
    var condition by remember { mutableStateOf(0) }
    var durability by remember { mutableStateOf(0) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePhotoUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Player") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { launcher.launch("image/*") }
                        .align(Alignment.CenterHorizontally)
                ) {
                    if (profilePhotoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(profilePhotoUri),
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Select Photo",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(50.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    item {
                        TextFieldComponent(
                            stateValue = firstName,
                            label = "First Name",
                            onValueChange = { firstName = it },
                            painterResource = painterResource(id = R.drawable.ic_person)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextFieldComponent(
                            stateValue = lastName,
                            label = "Last Name",
                            onValueChange = { lastName = it },
                            painterResource = painterResource(id = R.drawable.ic_person)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextFieldComponent(
                            stateValue = position,
                            label = "Position",
                            onValueChange = { position = it },
                            painterResource = painterResource(id = R.drawable.ic_position)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Speed: $speed", fontSize = 16.sp)
                        Slider(
                            value = speed.toFloat(),
                            onValueChange = { speed = it.toInt() },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Condition: $condition", fontSize = 16.sp)
                        Slider(
                            value = condition.toFloat(),
                            onValueChange = { condition = it.toInt() },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Focus: $focus", fontSize = 16.sp)
                        Slider(
                            value = focus.toFloat(),
                            onValueChange = { focus = it.toInt() },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Durability: $durability", fontSize = 16.sp)
                        Slider(
                            value = durability.toFloat(),
                            onValueChange = { durability = it.toInt() },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onAddPlayer(
                    PlayerData(
                        profilePhotoUrl = profilePhotoUri.toString(),
                        firstName = firstName,
                        lastName = lastName,
                        position = position,
                        competitionType = competitionName,
                        speed = speed,
                        focus = focus,
                        condition = condition,
                        durability = durability,
                        totalSkillRating = speed + focus + condition + durability
                    )
                )

                updateList()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun UpdatePlayerDialog(
    playerData: PlayerData,
    onDismiss: () -> Unit,
    onUpdatePlayer: (PlayerData) -> Unit,
    viewModel: PlayerListViewModel
) {
    var profilePhotoUri by remember { mutableStateOf<Uri?>(playerData.profilePhotoUrl.toUri()) }
    var firstName by remember { mutableStateOf(playerData.firstName) }
    var lastName by remember { mutableStateOf(playerData.lastName) }
    var position by remember { mutableStateOf(playerData.position) }
    var speed by remember { mutableStateOf(playerData.speed) }
    var focus by remember { mutableStateOf(playerData.focus) }
    var condition by remember { mutableStateOf(playerData.condition) }
    var durability by remember { mutableStateOf(playerData.durability) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            profilePhotoUri = uri
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Player") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { launcher.launch("image/*") }
                        .align(Alignment.CenterHorizontally)
                ) {
                    if (profilePhotoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(profilePhotoUri),
                            contentDescription = "Profile Photo",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Select Photo",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(50.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    item {
                        TextFieldComponent(
                            stateValue = firstName,
                            label = "First Name",
                            onValueChange = { firstName = it },
                            painterResource = painterResource(id = R.drawable.ic_person)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextFieldComponent(
                            stateValue = lastName,
                            label = "Last Name",
                            onValueChange = { lastName = it },
                            painterResource = painterResource(id = R.drawable.ic_person)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextFieldComponent(
                            stateValue = position,
                            label = "Position",
                            onValueChange = { position = it },
                            painterResource = painterResource(id = R.drawable.ic_position)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Speed: $speed", fontSize = 16.sp)
                        Slider(
                            value = speed.toFloat(),
                            onValueChange = { speed = it.toInt() },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Condition: $condition", fontSize = 16.sp)
                        Slider(
                            value = condition.toFloat(),
                            onValueChange = { condition = it.toInt() },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Focus: $focus", fontSize = 16.sp)
                        Slider(
                            value = focus.toFloat(),
                            onValueChange = { focus = it.toInt() },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Durability: $durability", fontSize = 16.sp)
                        Slider(
                            value = durability.toFloat(),
                            onValueChange = { durability = it.toInt() },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (profilePhotoUri != playerData.profilePhotoUrl.toUri()) {
                    profilePhotoUri?.let { uri ->
                        viewModel.updatePlayerImage(
                            uri = uri,
                            onSuccess = { downloadUrl ->
                                onUpdatePlayer(
                                    playerData.copy(
                                        firstName = firstName,
                                        lastName = lastName,
                                        position = position,
                                        speed = speed,
                                        focus = focus,
                                        condition = condition,
                                        durability = durability,
                                        totalSkillRating = speed + focus + condition + durability,
                                        profilePhotoUrl = downloadUrl
                                    )
                                )
                            },
                            onFailure = { /* Handle failure */ }
                        )
                    }
                } else {
                    onUpdatePlayer(
                        playerData.copy(
                            firstName = firstName,
                            lastName = lastName,
                            position = position,
                            speed = speed,
                            focus = focus,
                            condition = condition,
                            durability = durability,
                            totalSkillRating = speed + focus + condition + durability
                        )
                    )
                }
            }) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
