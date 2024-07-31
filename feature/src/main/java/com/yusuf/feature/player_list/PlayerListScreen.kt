package com.yusuf.feature.player_list

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.yusuf.component.LoadingLottie
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
                        if (profilePhotoUrl != null) {
                            viewModel.addPlayer(
                                PlayerData(
                                    profilePhotoUrl = profilePhotoUrl.toString(),
                                    firstName = firstName,
                                    lastName = lastName,
                                    position = position,
                                    skillRating = skillRating
                                ),
                                profilePhotoUrl
                            )
                        }
                        showAddPlayerDialog = false
                    }
                )
            }

            if (showUpdatePlayerDialog && playerDataToUpdate != null) {
                UpdatePlayerDialog(
                    playerData = playerDataToUpdate!!,
                    onDismiss = { showUpdatePlayerDialog = false },
                    onUpdatePlayer = { updatedPlayerData, imageUri ->
                        if (imageUri != null) {
                            viewModel.updatePlayerById(updatedPlayerData.id, updatedPlayerData, imageUri)
                        }
                        showUpdatePlayerDialog = false
                    }
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
            playerData.profilePhotoUrl?.let { url ->
                Image(
                    painter = rememberImagePainter(url),
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
            Text(text = "Skill Rating: ${playerData.skillRating}")
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
    onDismiss: () -> Unit,
    onAddPlayer: (Uri?, String, String, String, Int) -> Unit
) {
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var skillRating by remember { mutableIntStateOf(0) }

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
                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Select Profile Photo")
                }
                // Image preview and other input fields...
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") }
                )
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") }
                )
                TextField(
                    value = position,
                    onValueChange = { position = it },
                    label = { Text("Position") }
                )
                TextField(
                    value = skillRating.toString(),
                    onValueChange = { skillRating = it.toIntOrNull() ?: 0 },
                    label = { Text("Skill Rating") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onAddPlayer(profilePhotoUri, firstName, lastName, position, skillRating)
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
    onUpdatePlayer: (PlayerData, Uri?) -> Unit
) {
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var firstName by remember { mutableStateOf(playerData.firstName) }
    var lastName by remember { mutableStateOf(playerData.lastName) }
    var position by remember { mutableStateOf(playerData.position) }
    var skillRating by remember { mutableIntStateOf(playerData.skillRating) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePhotoUri = uri
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
                Button(onClick = { launcher.launch("image/*") }) {
                    Text("Select Profile Photo")
                }
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") }
                )
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") }
                )
                TextField(
                    value = position,
                    onValueChange = { position = it },
                    label = { Text("Position") }
                )
                TextField(
                    value = skillRating.toString(),
                    onValueChange = { skillRating = it.toIntOrNull() ?: 0 },
                    label = { Text("Skill Rating") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onUpdatePlayer(playerData.copy(firstName = firstName, lastName = lastName, position = position, skillRating = skillRating), profilePhotoUri)
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