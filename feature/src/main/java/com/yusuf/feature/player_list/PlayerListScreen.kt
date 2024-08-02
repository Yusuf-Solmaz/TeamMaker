package com.yusuf.feature.player_list

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.yusuf.component.LoadingLottie
import com.yusuf.component.TextFieldComponent
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import com.yusuf.feature.player_list.viewmodel.PlayerListViewModel
import com.yusuf.utils.SharedPreferencesHelper

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayerListScreen(
    viewModel: PlayerListViewModel = hiltViewModel()
) {
    val playerListUiState by viewModel.playerListUIState.collectAsState()

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
            FloatingActionButton(onClick = { showAddPlayerDialog = true }) {
                Text(text = "Add Player")
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
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
                                        viewModel.deletePlayerById(id,competitionName.toString())
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
                    onAddPlayer = { playerData  ->
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
                        viewModel.getPlayersByCompetitionType(competitionName.toString()) // Refresh the list
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

@Composable
fun PlayerListItem(
    playerData: PlayerData,
    onDelete: (String) -> Unit,
    onUpdatePlayer: (PlayerData) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            playerData.profilePhotoUrl.let { url ->
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = "Name: ${playerData.firstName} ${playerData.lastName}")
            Text(text = "Position: ${playerData.position}")
            Text(text = "Total Skill Rating: ${playerData.totalSkillRating}")
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = { onDelete(playerData.id) }) {
                    Text(text = "Delete")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onUpdatePlayer(playerData) }) {
                    Text(text = "Edit")
                }
            }
        }
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
                        .align(Alignment.CenterHorizontally) // Ortalamayı sağlar
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
                onAddPlayer(PlayerData(
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
                ))

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
                                        profilePhotoUrl = downloadUrl.toString()
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
