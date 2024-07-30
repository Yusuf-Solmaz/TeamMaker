package com.yusuf.feature.home

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.yusuf.component.LoadingLottie
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.R
import com.yusuf.feature.auth.login.viewmodel.LoginViewModel
import com.yusuf.feature.home.viewmodel.CompetitionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChooseSportScreen(
    navController: NavController,
    competitionViewModel: CompetitionViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val addDeleteState by competitionViewModel.addDeleteState.collectAsState()
    val getAllState by competitionViewModel.getAllState.collectAsState()
    val openDialog = remember { mutableStateOf(false) }
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        competitionViewModel.getAllCompetitions()
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri.value = uri
        }
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { openDialog.value = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Competition")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "Choose Sport") },
                actions = {
                    IconButton(onClick = {
                        loginViewModel.signOut()
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (openDialog.value) {
                    AddCompetitionDialog(
                        onDismiss = { openDialog.value = false },
                        onSave = { competitionName ->
                            selectedImageUri.value?.let { uri ->
                                competitionViewModel.uploadImageAndAddCompetition(uri, competitionName)
                            }
                        },
                        onImagePick = {
                            imagePickerLauncher.launch("image/*")
                        }
                    )
                }

                when (val state = getAllState.result) {
                    is RootResult.Loading -> LoadingLottie(R.raw.loading_anim)
                    is RootResult.Success -> {
                        val competitions = state.data ?: emptyList()
                        LazyColumn(
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            items(competitions) { competition ->
                                CompetitionCard(
                                    competition = competition,
                                    onClick = {
                                        navController.navigate("options")
                                    },
                                    onDelete = {
                                        Log.d(
                                            "ChooseSportScreen",
                                            "Delete button clicked for competition: ${competition.competitionId}"
                                        )
                                        competitionViewModel.deleteCompetition(competition)
                                    }
                                )
                            }
                        }
                    }

                    is RootResult.Error -> {
                        Text(text = state.message)
                    }

                    else -> {}
                }

                when (val addState = addDeleteState.result) {
                    is RootResult.Loading -> LoadingLottie(R.raw.loading_anim)
                    is RootResult.Success -> {
                        Log.d("ChooseSportScreen", "Competition added successfully: ${addState.data}")
                    }

                    is RootResult.Error -> {
                        Text(text = addState.message)
                    }

                    else -> {}
                }
            }
        }
    )
}
@Composable
fun AddCompetitionDialog(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    onImagePick: () -> Unit
) {
    var competitionName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Add Competition") },
        text = {
            Column {
                TextField(
                    value = competitionName,
                    onValueChange = { competitionName = it },
                    label = { Text("Competition Name") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onImagePick) {
                    Text("Pick Image")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(competitionName)
                onDismiss()
            }) {
                Text("Save")
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
fun CompetitionCard(
    competition: CompetitionData,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = competition.competitionName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                Image(
                    painter = rememberAsyncImagePainter(competition.competitionImageUrl),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Competition")
            }
        }
    }
}