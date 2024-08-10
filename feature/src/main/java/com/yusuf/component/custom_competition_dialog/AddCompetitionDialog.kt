package com.yusuf.component.custom_competition_dialog

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.feature.R
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.theme.CANCEL_RED
import com.yusuf.theme.DARK_BLUE
import com.yusuf.theme.DARK_GRAY
import com.yusuf.theme.DARK_GREEN
import com.yusuf.theme.DARK_RED
import com.yusuf.theme.GRAY
import com.yusuf.theme.GREEN
import com.yusuf.theme.LIGHT_GREEN
import com.yusuf.theme.RED
import com.yusuf.theme.YELLOW
import com.yusuf.utils.default_competition.Competition
import com.yusuf.utils.default_competition.predefinedCompetitions

@Composable
fun AddCompetitionDialog(
    onDismiss: () -> Unit,
    onSave: (Competition) -> Unit,
    onImagePick: () -> Unit,
    selectedImageUri: Uri?,
    context: Context
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCompetition by remember { mutableStateOf<Competition?>(null) }
    var customCompetitionName by remember { mutableStateOf("") }

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Competition",
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
                        .fillMaxWidth()
                        .height(128.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .clickable { onImagePick() }
                        .align(Alignment.CenterHorizontally)
                        .border(
                            width = 1.dp,
                            color = LIGHT_GREEN,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    selectedImageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    } ?: run {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Select Image",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(50.dp),
                            tint = LIGHT_GREEN
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .border(
                                width = 1.dp,
                                color = LIGHT_GREEN,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                expanded = true
                                customCompetitionName = ""
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = selectedCompetition?.competitionName ?: "Select Competition",
                            modifier = Modifier.weight(1f),
                            fontFamily = FontFamily(Font(R.font.oxygen, FontWeight.ExtraBold)),
                            color = LIGHT_GREEN
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.padding(8.dp),
                            tint = LIGHT_GREEN
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier
                            .background(Color.White)
                            .height(300.dp),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        predefinedCompetitions.forEach { competition ->
                            DropdownMenuItem(
                                text = { Text(competition.competitionName,
                                    color = LIGHT_GREEN) },
                                onClick = {
                                    selectedCompetition = competition
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedTextColor = LIGHT_GREEN,
                        focusedIndicatorColor = LIGHT_GREEN,
                        unfocusedIndicatorColor = LIGHT_GREEN,
                        unfocusedLabelColor = LIGHT_GREEN,
                        focusedLabelColor = LIGHT_GREEN,
                        cursorColor = LIGHT_GREEN,
                        unfocusedTextColor = LIGHT_GREEN
                    ),
                    value = customCompetitionName,
                    onValueChange = {
                        customCompetitionName = it
                        selectedCompetition = null
                    },
                    label = {
                        Text(
                            "Or Enter Custom Competition Name", style = TextStyle(
                                fontSize = 13.sp,
                                fontFamily = FontFamily(Font(R.font.oxygen, FontWeight.ExtraBold)),
                                color = LIGHT_GREEN
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            AuthButtonComponent(
                value = "Save", onClick = {
                    if (selectedImageUri == null) {
                        Toast.makeText(context, "Please choose an image.", Toast.LENGTH_SHORT)
                            .show()
                        return@AuthButtonComponent
                    }
                    if (selectedCompetition == null && customCompetitionName.isBlank()) {
                        Toast.makeText(
                            context,
                            "Please choose at least one competition",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@AuthButtonComponent
                    }

                    val competitionName =
                        selectedCompetition?.competitionName ?: customCompetitionName
                    if (competitionName.isNotBlank()) {
                        if (selectedCompetition != null) {
                            onSave(selectedCompetition!!)
                        } else {
                            onSave(
                                Competition(
                                    competitionName = competitionName,
                                    competitionFirstImage = 0,
                                    competitionTeamImage = 0
                                )
                            )
                        }
                        onDismiss()
                    }
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

