package com.yusuf.component.custom_player_dialog

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.yusuf.component.DividerTextComponent
import com.yusuf.component.TextFieldComponent
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.theme.CANCEL_RED
import com.yusuf.theme.DARK_BLUE
import com.yusuf.theme.DARK_RED
import com.yusuf.theme.LIGHT_GREEN
import com.yusuf.theme.LIGHT_RED
import com.yusuf.theme.PurpleGrey80
import com.yusuf.theme.RED
import com.yusuf.theme.YELLOW

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddPlayerDialog(
    competitionName: String,
    onDismiss: () -> Unit,
    onAddPlayer: (playerData: PlayerData) -> Unit,
    updateList: () -> Unit,
    context: Context
) {
    var profilePhotoUri by remember { mutableStateOf<Uri?>(null) }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var speed by remember { mutableStateOf(0) }
    var focus by remember { mutableStateOf(0) }
    var condition by remember { mutableStateOf(0) }
    var durability by remember { mutableStateOf(0) }
    var generalSkill by remember { mutableStateOf(0) }
    var isGeneralSkillUsed by remember { mutableStateOf(false) }

    if (generalSkill == 0) {
        isGeneralSkillUsed = false
    } else {
        isGeneralSkillUsed = true
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePhotoUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Launch image picker after permission is granted
            imagePickerLauncher.launch("image/*")
        } else {
            Toast.makeText(
                context,
                "Gallery permission is required to select an image.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val apiLevel = Build.VERSION.SDK_INT
    val hasGalleryPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Add Player",
                style = TextStyle(
                    color = LIGHT_GREEN,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.onboarding_title1))
                )
            )
        },
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
                        .background(Color.White)
                        .border(
                            width = 2.dp,
                            color = Color.LightGray,
                            shape = CircleShape
                        )
                        .clickable {
                            if (apiLevel < 33) {
                                if (hasGalleryPermission) {
                                    imagePickerLauncher.launch("image/*")
                                } else {
                                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            } else {
                                // For API level 33 and above, use the appropriate method if needed
                                imagePickerLauncher.launch("image/*")
                            }
                        }
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
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Select Photo",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(50.dp),
                            tint = Color.Gray
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
                            painterResource = painterResource(id = R.drawable.ic_person),
                            focusedLabelColor = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextFieldComponent(
                            stateValue = lastName,
                            label = "Last Name",
                            onValueChange = { lastName = it },
                            painterResource = painterResource(id = R.drawable.ic_person),
                            focusedLabelColor = Color.Gray,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextFieldComponent(
                            stateValue = position,
                            label = "Position",
                            onValueChange = { position = it },
                            painterResource = painterResource(id = R.drawable.ic_position),
                            focusedLabelColor = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "General Skill: $generalSkill",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontFamily = FontFamily(Font(R.font.oxygen, FontWeight.ExtraBold))
                        )
                        Slider(
                            colors = SliderDefaults.colors(
                                thumbColor = if (isGeneralSkillUsed) LIGHT_GREEN else LIGHT_RED,
                                activeTrackColor = LIGHT_GREEN,
                            ),
                            value = generalSkill.toFloat(),
                            onValueChange = { generalSkill = it.toInt() },
                            valueRange = 0f..10f,
                            steps = 10,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (!isGeneralSkillUsed) {
                            Spacer(modifier = Modifier.height(8.dp))
                            DividerTextComponent(
                                textColor = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Speed: $speed",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.oxygen, FontWeight.ExtraBold)),
                            )
                            Slider(
                                colors = SliderDefaults.colors(
                                    thumbColor = if (speed != 0) LIGHT_GREEN else LIGHT_RED,
                                    activeTrackColor = LIGHT_GREEN
                                ),
                                value = speed.toFloat(),
                                onValueChange = { speed = it.toInt() },
                                valueRange = 0f..10f,
                                steps = 10,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Condition: $condition",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.oxygen, FontWeight.ExtraBold))
                            )
                            Slider(
                                colors = SliderDefaults.colors(
                                    thumbColor = if (condition != 0) LIGHT_GREEN else LIGHT_RED,
                                    activeTrackColor = LIGHT_GREEN
                                ),
                                value = condition.toFloat(),
                                onValueChange = { condition = it.toInt() },
                                valueRange = 0f..10f,
                                steps = 10,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Focus: $focus",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.oxygen, FontWeight.ExtraBold))
                            )
                            Slider(
                                colors = SliderDefaults.colors(
                                    thumbColor = if (focus != 0) LIGHT_GREEN else LIGHT_RED,
                                    activeTrackColor = LIGHT_GREEN
                                ),
                                value = focus.toFloat(),
                                onValueChange = { focus = it.toInt() },
                                valueRange = 0f..10f,
                                steps = 10,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Durability: $durability",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily(Font(R.font.oxygen, FontWeight.ExtraBold))
                            )
                            Slider(
                                colors = SliderDefaults.colors(
                                    thumbColor = if (durability != 0) LIGHT_GREEN else LIGHT_RED,
                                    activeTrackColor = LIGHT_GREEN
                                ),
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
            AuthButtonComponent(
                value = "Add", onClick = {
                    if (profilePhotoUri == null || firstName.isBlank() || lastName.isBlank() || position.isBlank()) {
                        Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_SHORT)
                            .show()
                        return@AuthButtonComponent
                    }
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
                            generalSkill = generalSkill,
                            totalSkillRating = (speed + focus + condition + durability) / 4 + generalSkill
                        )
                    )
                    updateList()
                },
                modifier = Modifier.width(70.dp),
                fillMaxWidth = false,
                heightIn = 40.dp,
                firstColor = LIGHT_GREEN,
                secondColor = DARK_BLUE
            )
        },
        dismissButton = {
            AuthButtonComponent(
                value = "Cancel",
                onClick = { onDismiss() },
                modifier = Modifier.width(80.dp),
                fillMaxWidth = false,
                heightIn = 40.dp,
                firstColor = CANCEL_RED,
                secondColor = DARK_RED
            )
        }
    )
}
