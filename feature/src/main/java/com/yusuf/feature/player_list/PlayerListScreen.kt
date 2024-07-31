package com.yusuf.feature.player_list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yusuf.component.LoadingLottie
import com.yusuf.component.TextFieldComponent
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import com.yusuf.feature.player_list.viewmodel.PlayerListViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayerListScreen(
    viewModel: PlayerListViewModel = hiltViewModel()
) {
    val playerListUiState by viewModel.playerListUIState.collectAsState()

    var showAddPlayerDialog by remember { mutableStateOf(false) }
    var showUpdatePlayerDialog by remember { mutableStateOf(false) }
    var playerDataToUpdate by remember { mutableStateOf<PlayerData?>(null) }

    LaunchedEffect(true) {
        viewModel.getAllPlayers()
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

                            if (playerListUiState.playerList?.isEmpty() == true){
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
                                        viewModel.deletePlayerById(id)
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
                    onDismiss = { showAddPlayerDialog = false },
                    onAddPlayer = { profilePhotoUrl, firstName, lastName, position, skillRating ->
                        viewModel.addPlayer(
                            PlayerData(
                                profilePhotoUrl = profilePhotoUrl,
                                firstName = firstName,
                                lastName = lastName,
                                position = position,
                                skillRating = skillRating
                            )
                        )
                        showAddPlayerDialog = false
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
                    }
                )
            }
        }
    )
}
@Composable
fun PlayerListItem(playerData: PlayerData, onDelete: (String) -> Unit, onUpdatePlayer: (PlayerData) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${playerData.firstName} ${playerData.lastName}")
            Text(text = "Position: ${playerData.position}")
            Text(text = "Skill Rating: ${playerData.skillRating}")
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = { onDelete(playerData.id) }) {
                Text(text = "Delete")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { onUpdatePlayer(playerData) }) {
                    Text(text = "Edit")
                }
            }
        }
    }
}

@Composable
fun AddPlayerDialog(
    onDismiss: () -> Unit,
    onAddPlayer: (String, String, String, String, Int) -> Unit
) {
    var profilePhotoUrl by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var skillRating by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Player") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextFieldComponent(
                    stateValue = profilePhotoUrl,
                    label = "Profile Photo",
                    onValueChange = { profilePhotoUrl = it },
                    painterResource = painterResource(id = R.drawable.ic_photo)  // Replace with your icon
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextFieldComponent(
                    stateValue = firstName,
                    label = "First Name",
                    onValueChange = { firstName = it },
                    painterResource = painterResource(id = R.drawable.ic_person)  // Replace with your icon
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextFieldComponent(
                    stateValue = lastName,
                    label = "Last Name",
                    onValueChange = { lastName = it },
                    painterResource = painterResource(id = R.drawable.ic_person)  // Replace with your icon
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextFieldComponent(
                    stateValue = position,
                    label = "Position",
                    onValueChange = { position = it },
                    painterResource = painterResource(id = R.drawable.ic_position)  // Replace with your icon
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Skill Rating: $skillRating", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Slider(
                    value = skillRating.toFloat(),
                    onValueChange = { skillRating = it.toInt() },
                    valueRange = 0f..10f,
                    steps = 10,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (profilePhotoUrl.isNotBlank() &&
                        firstName.isNotBlank() &&
                        lastName.isNotBlank() &&
                        position.isNotBlank()
                    ) {
                        onAddPlayer(profilePhotoUrl, firstName, lastName, position, skillRating)
                    }
                }
            ) {
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
    onUpdatePlayer: (PlayerData) -> Unit
){
    var profilePhotoUrl by remember { mutableStateOf(playerData.profilePhotoUrl) }
    var firstName by remember { mutableStateOf(playerData.firstName) }
    var lastName by remember { mutableStateOf(playerData.lastName) }
    var position by remember { mutableStateOf(playerData.position) }
    var skillRating by remember { mutableStateOf(playerData.skillRating) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Player") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextFieldComponent(
                    stateValue = profilePhotoUrl,
                    label = "Profile Photo",
                    onValueChange = { profilePhotoUrl = it },
                    painterResource = painterResource(id = R.drawable.ic_photo)
                )
                Spacer(modifier = Modifier.height(8.dp))
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
                Text("Skill Rating: $skillRating", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Slider(
                    value = skillRating.toFloat(),
                    onValueChange = { skillRating = it.toInt() },
                    valueRange = 0f..10f,
                    steps = 10,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (profilePhotoUrl.isNotBlank() &&
                        firstName.isNotBlank() &&
                        lastName.isNotBlank() &&
                        position.isNotBlank()
                    ) {
                        onUpdatePlayer(
                            PlayerData(
                                id = playerData.id,
                                profilePhotoUrl = profilePhotoUrl,
                                firstName = firstName,
                                lastName = lastName,
                                position = position,
                                skillRating = skillRating
                            )
                        )
                    }
                }
            ){
                Text("Edit")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
