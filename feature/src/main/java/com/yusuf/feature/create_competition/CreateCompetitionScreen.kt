package com.yusuf.feature.create_competition

import SelectPlayerScreen
import androidx.compose.ui.Modifier
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yusuf.feature.create_competition.location.LocationScreen
import java.time.LocalDate



import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CreateCompetitionScreen(navController: NavController) {
    var selectedTime by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 75.dp),
            horizontalArrangement = Arrangement.spacedBy(25.dp), // Equal spacing
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time Picker with equal weight
            TimePicker(
                onTimeSelected = { time ->
                    selectedTime = time
                },
                modifier = Modifier.weight(1f) // Equal width
            )

            // Date Picker with equal weight
            DatePickerWithDialog(
                onDateSelected = { date ->
                    selectedDate = date
                },
           //     modifier = Modifier.weight(1f) // Equal width
            )
        }

        // Location section below Time and Date
        LocationScreen(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Player selection screen
        SelectPlayerScreen(navController, timePicker = selectedTime)
    }
}

