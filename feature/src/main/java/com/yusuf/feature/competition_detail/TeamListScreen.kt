package com.yusuf.feature.competition_detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.theme.DARK_BLUE
import com.yusuf.theme.DARK_GREEN
import com.yusuf.theme.GREEN
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamListScreen(
    firstTeam: List<PlayerData>? = null,
    secondTeam: List<PlayerData>? = null
) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val coroutineScope = rememberCoroutineScope()

    // Function to calculate average skill rating
    fun calculateAverageSkillRating(players: List<PlayerData>): Double {
        if (players.isEmpty()) return 0.0
        return players.sumOf { it.totalSkillRating } / players.size.toDouble()
    }

    Column(
        Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                divider = {},
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier
                            .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                            .height(2.dp),
                        color = Color.White
                    )
                }
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    },
                    modifier = Modifier
                        .background(if (pagerState.currentPage == 0) DARK_GREEN else MaterialTheme.colorScheme.background)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "First Team",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pagerState.currentPage == 0) Color.White else DARK_GREEN
                        )
                    )
                }
                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    },
                    modifier = Modifier
                        .background(if (pagerState.currentPage == 1) DARK_GREEN else MaterialTheme.colorScheme.background)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Second Team",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pagerState.currentPage == 1) Color.White else DARK_GREEN
                        )
                    )
                }
            }
        }

        // Display average skill rating based on the current page
        HorizontalPager(state = pagerState) { page ->
            val team = when (page) {
                0 -> firstTeam ?: emptyList()
                1 -> secondTeam ?: emptyList()
                else -> emptyList()
            }
            val averageSkill = calculateAverageSkillRating(team)

            Column {
                Text(
                    text = "Average Skill Rating: %.2f".format(averageSkill),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DARK_BLUE
                    ),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)
                )
                TeamList(team = team)
            }
        }
    }
}

@Composable
fun TeamList(team: List<PlayerData>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(8.dp)
    ) {
        items(team.size) { index ->
            val player = team[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .height(200.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(player.profilePhotoUrl),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .width(70.dp)
                            .height(70.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Player details
                    Column {
                        Text(
                            text = "${player.firstName} ${player.lastName}",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DARK_BLUE
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Position: ${player.position}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = DARK_BLUE
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Total Skill Rating: ${player.totalSkillRating}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = DARK_GREEN
                            ),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { (player.totalSkillRating / 10f).coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth(),
                            color = GREEN,
                        )
                    }
                }
            }
        }
    }
}



