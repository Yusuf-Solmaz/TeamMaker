package com.yusuf.feature.create_competition.location

import android.content.Intent
import android.Manifest
import android.location.Location
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.yusuf.component.LoadingLottie
import com.yusuf.feature.R
import kotlinx.coroutines.launch

@Composable
fun LocationScreen(
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel = hiltViewModel(),
    // Location - location
    location: (location: Location, locationName: String) -> Unit
) {
    val uiState by viewModel.locationUIState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var showPermissionDialog by remember { mutableStateOf(false) }
    var permissionRequested by remember { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            coroutineScope.launch {
                viewModel.fetchLocation()
            }
        } else {
            showPermissionDialog = true
            Log.e("LocationScreen", "Location permissions denied")
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionRequested) {
            if (viewModel.checkPermissions()) {
                viewModel.fetchLocation()
            } else  {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                permissionRequested = true
            }
        }
    }

    if (showPermissionDialog) {
        PermissionExplanationDialog(
            onDismiss = { showPermissionDialog = false },
            onConfirm = {
                showPermissionDialog = false
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            },
            onSettings = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", context.packageName, null)
                startActivity(context, intent, null)
            }
        )
    }

    Column(
        modifier = modifier.padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            uiState.isLoading -> {
                LocationCard(loading = true)
            }
            uiState.error != null -> {
                LocationCard(error = uiState.error)
            }
            uiState.location != null -> {
                location(uiState.location!!, uiState.locationName!!)
                LocationCard(
                    locationName = uiState.locationName ?: "Unknown city"
                )
            }
            else -> {
                Text("No location data available")
            }
        }
    }
}

@Composable
fun LocationCard(
    loading: Boolean = false,
    locationName: String? = null,
    error: String? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(width = 200.dp, height = 80.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ){
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            )  {
                if (loading) {
                    LoadingLottie(R.raw.image_loading)
                }
                if (error != null) {
                    Text(
                        text = error,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.splash_title_font)),
                            fontSize = 18.sp,
                            color = Color.Black,
                        )
                    )
                }
                if(locationName!=null){
                    Image(
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = "Location icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = locationName,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.splash_title_font)),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun PermissionExplanationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onSettings: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Location Permission Required") },
        text = { Text("This app requires location permission to function correctly. Please grant the permission.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Grant Permission")
            }
        },
        dismissButton = {
            TextButton(onClick = onSettings) {
                Text("Settings")
            }
        }
    )
}