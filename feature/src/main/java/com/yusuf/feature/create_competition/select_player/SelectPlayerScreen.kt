import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.domain.model.competition_detail.CompetitionDetail
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import com.yusuf.feature.create_competition.select_player.viewmodel.SelectPlayerViewModel
import com.yusuf.feature.create_competition.select_player.viewmodel.TeamBalancerViewModel
import com.yusuf.navigation.NavigationGraph
import com.yusuf.theme.DARK_GREEN
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.utils.SharedPreferencesHelper

@Composable
fun SelectPlayerScreen(
    navController: NavController,
    sharedViewModel: TeamBalancerViewModel = hiltViewModel(),
    timePicker: String = ""
) {
    val viewModel: SelectPlayerViewModel = hiltViewModel()
    val playerListUiState by viewModel.playerListUIState.collectAsState()
    val teamBalancerUIState by sharedViewModel.teamBalancerUiState.collectAsState()

    val context = LocalContext.current
    val sharedPreferencesHelper = remember { SharedPreferencesHelper(context) }
    val selectedPlayers = remember { mutableStateListOf<PlayerData>() }
    val competitionName = sharedPreferencesHelper.competitionName


    LaunchedEffect(true) {
        viewModel.getPlayersByCompetitionType(competitionName.toString())
    }

    LaunchedEffect(teamBalancerUIState) {
        if (teamBalancerUIState.teams != null) {
            Log.d("SelectPlayerScreen", "Teams are ready: ${teamBalancerUIState.teams}")

            val route = NavigationGraph.getCompetitionDetailsRoute(CompetitionDetail(
                selectedTime = timePicker,
                firstBalancedTeam = teamBalancerUIState.teams!!.first,
                secondBalancedTeam = teamBalancerUIState.teams!!.second
            ))
            navController.navigate(route)
        }
        if (teamBalancerUIState.isLoading) {
            Log.d("SelectPlayerScreen", "Loading teams...")
        }
        if (teamBalancerUIState.errorMessage != null) {
            Log.d("SelectPlayerScreen", "Teams are not ready yet.")
        }
    }

    if (teamBalancerUIState.isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Creating teams...",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    color = APPBAR_GREEN,
                    fontFamily = FontFamily(Font(R.font.onboarding_title1))
                )
            )
        }
    } else {
        Column(Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(playerListUiState.playerList!!.size) { index ->
                    val player = playerListUiState.playerList!![index]
                    val isSelected = player in selectedPlayers

                    var isFlipped by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier
                            .background(Color.Transparent)
                            .padding(8.dp)
                            .border(
                                width = 2.dp,
                                color = if (isSelected) APPBAR_GREEN else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = {
                                        // Trigger selection on single tap
                                        val pressStart = System.currentTimeMillis()
                                        val result = tryAwaitRelease()
                                        val pressEnd = System.currentTimeMillis()
                                        val isLongPress = pressEnd - pressStart > 500 // Long press threshold

                                        if (isLongPress) {
                                            isFlipped = !isFlipped
                                        } else {
                                            if (isSelected) {
                                                selectedPlayers.remove(player)
                                            } else {
                                                selectedPlayers.add(player)
                                            }
                                        }
                                    }
                                )
                            }
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier.background(Color.White)
                        ) {
                            this@Card.AnimatedVisibility(
                                visible = !isFlipped,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                FrontCardContent(player, { isFlipped = !isFlipped })
                            }

                            this@Card.AnimatedVisibility(
                                visible = isFlipped,
                                enter = fadeIn(),
                                exit = fadeOut(),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                BackCardContent(player, { isFlipped = !isFlipped })
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (selectedPlayers.isEmpty()) {
                        Toast.makeText(context, "Please select at least one player", Toast.LENGTH_SHORT).show()
                    } else if (selectedPlayers.size % 2 != 0) {
                        Toast.makeText(context, "The number of selected players must be even", Toast.LENGTH_SHORT).show()
                    } else if (timePicker.isEmpty()) {
                        Toast.makeText(context, "Please select a time", Toast.LENGTH_SHORT).show()
                    } else {
                        sharedViewModel.createBalancedTeams(selectedPlayers)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(24.dp),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .width(250.dp)
                        .heightIn(34.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    APPBAR_GREEN,
                                    DARK_GREEN
                                )
                            ), shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
fun FrontCardContent(player: PlayerData, onFlip: () -> Unit) {
    val compositionLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.image_loading))
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.player_card),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            SubcomposeAsyncImage(
                model = player.profilePhotoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                loading = {
                    LottieAnimation(
                        composition = compositionLottie,
                        modifier = Modifier.size(100.dp),
                        iterations = Int.MAX_VALUE
                    )
                }
            )
            Text(
                text = "${player.firstName} ${player.lastName}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Rating: ${player.totalSkillRating}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DARK_GREEN,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BackCardContent(player: PlayerData, onFlip: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Speed: ${player.speed}", fontSize = 16.sp)
            Text(text = "Focus: ${player.focus}", fontSize = 16.sp)
            Text(text = "Condition: ${player.condition}", fontSize = 16.sp)
            Text(text = "Durability: ${player.durability}", fontSize = 16.sp)
        }
    }
}