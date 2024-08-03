import android.util.Log
import android.widget.TimePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.yusuf.component.LoadingLottie
import com.yusuf.domain.model.competition_detail.CompetitionDetail
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import com.yusuf.feature.create_match.player.viewmodel.SelectPlayerViewModel
import com.yusuf.feature.match_detail.SharedViewModel
import com.yusuf.navigation.NavigationGraph
import com.yusuf.theme.DarkGreen
import com.yusuf.theme.Green
import com.yusuf.utils.SharedPreferencesHelper

@Composable
fun SelectPlayerScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    timePicker: String = ""
) {

    val viewModel: SelectPlayerViewModel = hiltViewModel()
    val playerListUiState by viewModel.playerListUIState.collectAsState()
    val teamBalancerUIState by sharedViewModel.teamBalancerUiState.collectAsState()

    val context = LocalContext.current
    val sharedPreferencesHelper = remember { SharedPreferencesHelper(context) }
    val selectedPlayers = remember { mutableStateListOf<PlayerData>() }
    val competitionName = sharedPreferencesHelper.competitionName
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.image_loading))

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
        Log.d("SelectPlayerScreenUI", "${teamBalancerUIState}")
        Log.d("SelectPlayerScreenUI", "${teamBalancerUIState.isLoading}")
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Creating teams...", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                style = TextStyle(
                    color = Green,
                    fontFamily = FontFamily(
                        Font(
                            R.font.onboarding_title1
                        )
                    )
                )  )
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

                    Card(
                        modifier = Modifier
                            .background(Color.Transparent)
                            .padding(8.dp)
                            .border(
                                width = 2.dp,
                                color = if (isSelected) Green else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                if (isSelected) {
                                    selectedPlayers.remove(player)
                                } else {
                                    selectedPlayers.add(player)
                                }
                            }
                            .fillMaxWidth(),
                    ) {
                        Box(
                            modifier = Modifier.background(Color.White)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.player_card),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

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
                                            composition,
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
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Column {
                                        Text(text = "FOC: ${player.focus}", fontSize = 12.sp)
                                        Text(text = "CON: ${player.condition}", fontSize = 12.sp)
                                    }
                                    Column {
                                        Text(text = "SPE: ${player.speed}", fontSize = 12.sp)
                                        Text(text = "DUR: ${player.durability}", fontSize = 12.sp)
                                    }
                                }
                            }
                            Text(
                                text = player.totalSkillRating.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(start = 20.dp, top = 40.dp)
                                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    Log.d("SelectPlayerScreen:  ", "selectedPlayers: ${selectedPlayers.size}")
                    sharedViewModel.createBalancedTeams(selectedPlayers)
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
                                    Green,
                                    DarkGreen
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

