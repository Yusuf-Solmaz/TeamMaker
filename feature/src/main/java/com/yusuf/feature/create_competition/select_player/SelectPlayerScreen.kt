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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.yusuf.feature.create_competition.select_player.BackCardContent
import com.yusuf.feature.create_competition.select_player.FrontCardContent
import com.yusuf.feature.create_competition.select_player.viewmodel.SelectPlayerViewModel
import com.yusuf.feature.create_competition.select_player.viewmodel.TeamBalancerViewModel
import com.yusuf.navigation.NavigationGraph
import com.yusuf.theme.DARK_GREEN
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.utils.SharedPreferencesHelper

@Composable
fun SelectPlayerScreen(
    navController: NavController,
    teamBalancerViewModel: TeamBalancerViewModel = hiltViewModel(),
    timePicker: String = ""
) {
    val viewModel: SelectPlayerViewModel = hiltViewModel()
    val playerListUiState by viewModel.playerListUIState.collectAsState()
    val teamBalancerUIState by teamBalancerViewModel.teamBalancerUiState.collectAsState()

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

            val route = NavigationGraph.getCompetitionDetailsRoute(
                CompetitionDetail(
                    selectedTime = timePicker,
                    firstBalancedTeam = teamBalancerUIState.teams!!.first,
                    secondBalancedTeam = teamBalancerUIState.teams!!.second
                )
            )
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
                            .clickable {
                                if (isSelected) {
                                    selectedPlayers.remove(player)
                                } else {
                                    selectedPlayers.add(player)
                                }
                            }
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White)
                        ) {
                            IconButton(
                                onClick = { isFlipped = !isFlipped },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 1.dp, end = 1.dp)
                                    .size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options"
                                )
                            }

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
                        Toast.makeText(
                            context,
                            "Please select at least one player",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (selectedPlayers.size % 2 != 0) {
                        Toast.makeText(
                            context,
                            "The number of selected players must be even",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (timePicker.isEmpty()) {
                        Toast.makeText(context, "Please select a time", Toast.LENGTH_SHORT).show()
                    } else {
                        teamBalancerViewModel.createBalancedTeams(selectedPlayers)
                    }
                },
                modifier = Modifier
                    .width(250.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
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


