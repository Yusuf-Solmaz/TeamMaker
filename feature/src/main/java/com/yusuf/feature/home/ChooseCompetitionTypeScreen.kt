package com.yusuf.feature.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.BackHandler
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.component.ConfirmationDialog
import com.yusuf.component.LoadingLottie
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.R
import com.yusuf.component.custom_competition_dialog.AddCompetitionDialog
import com.yusuf.component.custom_competition_dialog.UpdateCompetitionDialog
import com.yusuf.feature.home.slideable_image.ImageSliderScreen
import com.yusuf.feature.home.viewmodel.CompetitionViewModel
import com.yusuf.navigation.NavigationGraph
import com.yusuf.navigation.main_datastore.MainDataStore
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.theme.DARK_RED
import com.yusuf.theme.RED
import com.yusuf.utils.SharedPreferencesHelper
import com.yusuf.utils.default_competition.toCompetition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChooseCompetitionTypeScreen(
    navController: NavController,
    competitionViewModel: CompetitionViewModel = hiltViewModel()
) {
    val addDeleteState by competitionViewModel.addDeleteState.collectAsState()
    val getAllState by competitionViewModel.getAllState.collectAsState()
    val openDialog = remember { mutableStateOf(false) }
    val openUpdateDialog = remember { mutableStateOf(false) }
    val selectedCompetition = remember { mutableStateOf<CompetitionData?>(null) }
    var competitionDatatoUpdate by remember { mutableStateOf<CompetitionData?>(null) }

    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val mainDataStore = remember { MainDataStore(context) }
    val galleryPermissionGranted by mainDataStore.readGalleryPermission.collectAsState(initial = false)

    val sharedPreferencesHelper = SharedPreferencesHelper(context)

    BackHandler {
        (context as? Activity)?.finish()
    }

    // Initialize image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri.value = uri
    }

    // Initialize permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Save permission granted status in DataStore
            CoroutineScope(Dispatchers.IO).launch {
                mainDataStore.saveGalleryPermission(true)
            }
        } else {
            Toast.makeText(
                context,
                "Gallery permission is required to select an image.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    val apiLevel = Build.VERSION.SDK_INT
    LaunchedEffect(Unit) {
        competitionViewModel.getAllCompetitions()

        // Only request permission if needed (API level below 33)
        if (apiLevel < 33 && !galleryPermissionGranted) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                // Save permission granted status in DataStore
                CoroutineScope(Dispatchers.IO).launch {
                    mainDataStore.saveGalleryPermission(true)
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            if (getAllState.result !is RootResult.Loading) {
                FloatingActionButton(
                    onClick = { openDialog.value = true },
                    containerColor = APPBAR_GREEN,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Competition")
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (openDialog.value) {
                    AddCompetitionDialog(
                        onDismiss = { openDialog.value = false },
                        onSave = { competition ->
                            competitionViewModel.uploadImageAndAddCompetition(
                                selectedImageUri.value!!,
                                competition.competitionName
                            )
                        },
                        onImagePick = {
                            if (apiLevel >= 33 || galleryPermissionGranted) {
                                imagePickerLauncher.launch("image/*")
                            } else {
                                // Request permission if not granted
                                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                            }
                        },
                        selectedImageUri = selectedImageUri.value,
                        context = context
                    )
                }

                if (openUpdateDialog.value && selectedCompetition.value != null) {
                    competitionDatatoUpdate?.let { it1 ->
                        UpdateCompetitionDialog(
                            competitionData = it1,
                            onDismiss = { openUpdateDialog.value = false },
                            onUpdateCompetition = { competitionData ->
                                competitionViewModel.updateCompetition(
                                    competitionData.competitionId,
                                    competitionData,
                                    selectedImageUri.value
                                )
                                openUpdateDialog.value = false
                            },
                            onImagePick = {
                                if (apiLevel >= 33 || galleryPermissionGranted) {
                                    imagePickerLauncher.launch("image/*")
                                } else {
                                    // Request permission if not granted
                                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            },
                            selectedImageUri = selectedImageUri.value
                        )
                    }
                }

                when (val state = getAllState.result) {
                    is RootResult.Loading -> {
                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            LoadingLottie(R.raw.image_loading)
                        }
                    }

                    is RootResult.Success -> {
                        if (getAllState.competitions.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "You don't have any competitions yet.",
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

                        ImageSliderScreen()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                textAlign = TextAlign.Start,
                                text = "Competitions",
                                modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                                style = TextStyle(
                                    color = APPBAR_GREEN,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.main_title))
                                )
                            )
                        }

                        val competitions = state.data ?: emptyList()

                        LazyColumn(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .weight(1f)
                        ) {
                            items(competitions.reversed()) { competition ->
                                CompetitionCard(
                                    competition = competition,
                                    onClick = {
                                        val competitionData = competition.toCompetition()

                                        sharedPreferencesHelper.competitionName =
                                            competition.competitionName
                                        navController.navigate(
                                            NavigationGraph.getOptionsRoute(
                                                competitionData
                                            )
                                        )
                                    },
                                    onDelete = {
                                        Log.d(
                                            "ChooseSportScreen",
                                            "Delete button clicked for competition: ${competition.competitionId}"
                                        )
                                        competitionViewModel.deleteCompetition(competition)
                                    },
                                    onUpdate = {
                                        selectedCompetition.value = competition
                                        competitionDatatoUpdate = competition
                                        openUpdateDialog.value = true
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
                    is RootResult.Loading -> {
                        Column(Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally) {
                            LoadingLottie(R.raw.image_loading)
                        }
                    }

                    is RootResult.Success -> {
                        Log.d(
                            "ChooseSportScreen",
                            "Competition added/updated successfully: ${addState.data}"
                        )
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
fun CompetitionCard(
    competition: CompetitionData,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var deleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick()
                }
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.image_loading))
            var isImageLoading by remember { mutableStateOf(true) }

            SubcomposeAsyncImage(
                model = competition.competitionImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop,
                loading = {
                    LottieAnimation(
                        composition,
                        modifier = Modifier.size(100.dp),
                        iterations = Int.MAX_VALUE
                    )
                },
                onSuccess = {
                    isImageLoading = false
                },
                onError = {
                    isImageLoading = false
                }
            )
            if (!isImageLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                )

                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = competition.competitionName,
                        color = Color.White,
                        style = TextStyle(
                            fontSize = 35.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            shadow = Shadow(
                                color = Color(0xFF333333),
                                offset = androidx.compose.ui.geometry.Offset(4f, 4f),
                                blurRadius = 10f
                            )
                        )
                    )

                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp, 24.dp)
                        .background(
                            color = Color.Black.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )

                ) {
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expand",
                            tint = Color.White,
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = expanded) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AuthButtonComponent(
                    value = "Delete",
                    onClick = { deleteDialog = true },
                    fillMaxWidth = false,
                    modifier = Modifier.width(80.dp),
                    heightIn = 37.dp,
                    firstColor = RED,
                    secondColor = DARK_RED
                )

                AuthButtonComponent(
                    value = "Update",
                    onClick = onUpdate,
                    fillMaxWidth = false,
                    modifier = Modifier.width(80.dp),
                    heightIn = 37.dp
                )
            }
        }
        if (deleteDialog) {
            ConfirmationDialog(
                onDismiss = { deleteDialog = it },
                onConfirm = {
                    onDelete()
                    deleteDialog = false
                }
            )
        }
    }
}