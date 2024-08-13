package com.yusuf.feature.create_competition.date_time

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.yusuf.feature.R
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.theme.LIGHT_RED
import java.util.Calendar
import java.util.Locale

@Composable
fun TimePicker(
    onTimeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAdvancedExample by remember { mutableStateOf(false) }
    var selectedTime: String? by remember { mutableStateOf(null) }

    Card(
        modifier = Modifier
            .size(width = 100.dp, height = 100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Box(
            Modifier
                .fillMaxSize()
                .clickable {
                    showAdvancedExample = true
                }) {

            Column(
                modifier = modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = {
                    showAdvancedExample = true
                }) {
                    Icon(
                        painterResource(R.drawable.baseline_watch_later_24),
                        contentDescription = null
                    )
                }
                Text(
                    text = selectedTime ?: "Choose Time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }

        when {
            showAdvancedExample -> AdvancedTimePickerExample(
                onDismiss = { showAdvancedExample = false },
                onConfirm = { time ->
                    selectedTime = time
                    onTimeSelected(time)
                    showAdvancedExample = false
                },
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedTimePickerExample(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    var showDial by remember { mutableStateOf(true) }

    val toggleIcon = if (showDial) {
        Icons.Filled.Add
    } else {
        Icons.Filled.Build
    }
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color.Black,
            primaryContainer = Color.White,
            onPrimaryContainer = Color.Black,
            outline = Color.Black,
            secondary = Color.Black,
            onSecondary = Color.White,
            secondaryContainer = Color.White,
            onSurface = Color.Black,
            surfaceVariant = Color.White,
            outlineVariant = Color.Black
        )
    ) {
        AdvancedTimePickerDialog(
            onDismiss = { onDismiss() },
            onConfirm = {
                val hour = timePickerState.hour
                val minute = String.format("%02d", timePickerState.minute)
                val selectedTime = "$hour:$minute"
                onConfirm(selectedTime)
            },

            toggle = {
                IconButton(onClick = { showDial = !showDial }) {
                    Icon(
                        imageVector = toggleIcon,
                        contentDescription = "Time picker type toggle",
                    )
                }
            },
        ) {
            if (showDial) {
                androidx.compose.material3.TimePicker(state = timePickerState)
            } else {
                TimeInput(state = timePickerState)
            }
        }
    }
}

@Composable
fun AdvancedTimePickerDialog(
    title: String = "Select Time",
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier =
            Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.onboarding_title1))
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onDismiss) { Text("Cancel", color = LIGHT_RED) }
                    TextButton(onClick = onConfirm) { Text("OK", color = APPBAR_GREEN) }
                }
            }
        }
    }
}