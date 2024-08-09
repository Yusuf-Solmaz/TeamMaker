package com.yusuf.component.custom_competition_dialog

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.feature.R
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.theme.LIGHT_BLACK
import com.yusuf.theme.LIGHT_GREEN
import com.yusuf.utils.default_competition.Competition
import com.yusuf.utils.default_competition.predefinedCompetitions

@Composable
fun UpdateCompetitionDialog(
    competitionData: CompetitionData,
    onDismiss: () -> Unit,
    onUpdateCompetition: (CompetitionData) -> Unit,
    onImagePick: () -> Unit,
    selectedImageUri: Uri?,
    context: Context
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCompetition by remember { mutableStateOf<Competition?>(null) }
    var customCompetitionName by remember { mutableStateOf("") }

    LaunchedEffect(competitionData) {
        selectedCompetition = predefinedCompetitions.find { it.competitionName == competitionData.competitionName }
        customCompetitionName = if (selectedCompetition == null) competitionData.competitionName else ""
    }

    AlertDialog(
        containerColor = LIGHT_BLACK,
        onDismissRequest = onDismiss,
        title = { Text(text = "Update Competition",
            style = TextStyle(
                color = Color.White,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.main_title))
            )
        ) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable { onImagePick() }
                        .align(Alignment.CenterHorizontally)
                ) {
                    selectedImageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center
                        )
                    } ?: run {
                        Image(
                            painter = rememberAsyncImagePainter(competitionData.competitionImageUrl),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                expanded = true
                                customCompetitionName = ""
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedCompetition?.competitionName ?: "Select Competition",
                            modifier = Modifier.weight(1f),
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.padding(8.dp),
                            tint = Color.White
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier.background(Color.White)
                            .height(300.dp),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        predefinedCompetitions.forEach { competition ->
                            DropdownMenuItem(
                                text = { Text(competition.competitionName) },
                                onClick = {
                                    selectedCompetition = competition
                                    customCompetitionName = ""
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
                        focusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black,
                        unfocusedLabelColor =Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    value = customCompetitionName,
                    onValueChange = {
                        customCompetitionName = it
                        selectedCompetition = null
                    },
                    label = { Text("Or Enter Custom Competition Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {

                val competitionName = selectedCompetition?.competitionName ?: customCompetitionName
                if (competitionName.isNotBlank()) {
                    onUpdateCompetition(
                        competitionData.copy(
                            competitionName = competitionName
                        )
                    )
                    onDismiss()
                }
            },
                colors = ButtonDefaults.buttonColors(containerColor = APPBAR_GREEN)) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text("Cancel")
            }
        }
    )
}