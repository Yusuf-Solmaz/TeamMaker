package com.yusuf.feature.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.home.viewmodel.CompetitionViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChooseSportScreen(
    navController: NavController,
    competitionViewModel: CompetitionViewModel = hiltViewModel()
) {
    val addDeleteState by competitionViewModel.addDeleteState.collectAsState()
    val getAllState by competitionViewModel.getAllState.collectAsState()
    val openDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        competitionViewModel.getAllCompetitions()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { openDialog.value = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add/Delete Competition")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (openDialog.value) {
                AddCompetitionDialog(
                    onDismiss = { openDialog.value = false },
                    onSave = { competition ->
                        competitionViewModel.addCompetition(competition)
                    }
                )
            }

            when (val state = getAllState.result) {
                is RootResult.Loading -> CircularProgressIndicator()
                is RootResult.Success -> {
                    val competitions = state.data ?: emptyList()
                    LazyColumn {
                        items(competitions) { competition ->
                            CompetitionCard(
                                competition = competition,
                                onDelete = {
                                    Log.d("ChooseSportScreen", "Delete button clicked for competition: ${competition.competitionId}")
                                    competitionViewModel.deleteCompetition(competition)
                                }
                            )
                        }
                    }
                }
                is RootResult.Error -> {
                    Text(text = state.message ?: "An error occurred")
                }
                else -> {}
            }

            when (val addState = addDeleteState.result) {
                is RootResult.Loading -> CircularProgressIndicator()
                is RootResult.Success -> {
                    // Optional: Show a success message or take any other action
                }
                is RootResult.Error -> {
                    Text(text = addState.message ?: "An error occurred")
                }
                else -> {}
            }
        }
    }
}

@Composable
fun AddCompetitionDialog(
    onDismiss: () -> Unit,
    onSave: (CompetitionData) -> Unit
) {
    var competitionName by remember { mutableStateOf("") }
    var competitionImage by remember { mutableStateOf("") }

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
                TextField(
                    value = competitionImage,
                    onValueChange = { competitionImage = it },
                    label = { Text("Competition Image URL") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val competition = CompetitionData(
                    competitionName = competitionName,
                    competitionImageUrl = competitionImage
                )
                onSave(competition)
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
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
