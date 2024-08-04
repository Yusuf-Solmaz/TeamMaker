package com.yusuf.component.custom_player_dialog

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.yusuf.component.DividerTextComponent
import com.yusuf.component.TextFieldComponent
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import com.yusuf.feature.player_list.viewmodel.PlayerListViewModel

@Composable
fun UpdatePlayerDialog(
    playerData: PlayerData,
    onDismiss: () -> Unit,
    onUpdatePlayer: (PlayerData) -> Unit,
    viewModel: PlayerListViewModel,
    context: Context
) {
    var profilePhotoUri by remember { mutableStateOf<Uri?>(playerData.profilePhotoUrl.toUri()) }
    var firstName by remember { mutableStateOf(playerData.firstName) }
    var lastName by remember { mutableStateOf(playerData.lastName) }
    var position by remember { mutableStateOf(playerData.position) }
    var speed by remember { mutableStateOf(playerData.speed) }
    var focus by remember { mutableStateOf(playerData.focus) }
    var condition by remember { mutableStateOf(playerData.condition) }
    var durability by remember { mutableStateOf(playerData.durability) }
    var generalSkill by remember { mutableStateOf(playerData.generalSkill) }
    var isGeneralSkillUsed by remember { mutableStateOf(false) }

    if (generalSkill == 0) {
        isGeneralSkillUsed = false
    } else {
        isGeneralSkillUsed = true
    }

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
                        Text("General Point: $generalSkill", fontSize = 16.sp)
                        Slider(
                            value = generalSkill.toFloat(),
                            onValueChange = {
                                generalSkill = it.toInt()
                            },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (!isGeneralSkillUsed) {
                            Spacer(modifier = Modifier.height(8.dp))
                            DividerTextComponent()

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
            }
        },
        confirmButton = {
            Button(onClick = {

                if (profilePhotoUri == null) {
                    Toast.makeText(context,"Please select a profile photo.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

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
                                        speed =  speed,
                                        focus =  focus,
                                        condition =  condition,
                                        durability =  durability,
                                        generalSkill = generalSkill,
                                        totalSkillRating = (speed + focus + condition + durability)/4 + generalSkill,
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
                            condition =  condition,
                            durability =  durability,
                            generalSkill = generalSkill,
                            totalSkillRating = (speed + focus + condition + durability)/4 + generalSkill
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

