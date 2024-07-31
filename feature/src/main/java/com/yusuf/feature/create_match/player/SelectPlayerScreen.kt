import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import com.yusuf.feature.create_match.player.viewmodel.SelectPlayerViewModel
import com.yusuf.theme.Green

@Composable
fun SelectPlayerScreen() {
    val viewModel: SelectPlayerViewModel = hiltViewModel()
    val playerListUiState by viewModel.playerListUIState.collectAsState()

    LaunchedEffect(true) {
        viewModel.getAllPlayers()
    }

    val selectedPlayers = remember { mutableStateListOf<PlayerData>() }

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
                        .padding(8.dp)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) Color.Magenta else Color.Gray,
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
                    Box {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.players),
                                contentDescription = "${player.firstName} ${player.lastName}",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = "${player.firstName} ${player.lastName}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column {
                                    Text(text = "FOC: ${player.skillRating}", fontSize = 12.sp)
                                    Text(text = "CON: ${player.skillRating}", fontSize = 12.sp)
                                }
                                VerticalDivider(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .padding(horizontal = 8.dp),
                                    thickness = 1.dp,
                                    color = Color.Black
                                )
                                Column {
                                    Text(text = "SPE: ${player.skillRating}", fontSize = 12.sp)
                                    Text(text = "DUR: ${player.skillRating}", fontSize = 12.sp)
                                }
                            }
                        }
                        Text(
                            text = player.skillRating.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(8.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                                .background(Green, RoundedCornerShape(4.dp))
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
