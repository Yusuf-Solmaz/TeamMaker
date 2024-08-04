package com.yusuf.feature.competition_detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.theme.Green
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TeamListScreen(
    firstTeam: List<PlayerData>? = null,
    secondTeam: List<PlayerData>? = null
) {
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()

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
                        .background(if (pagerState.currentPage == 0) Green else MaterialTheme.colorScheme.background)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "First Team",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (pagerState.currentPage == 0) Color.White else Green
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
                        .background(if (pagerState.currentPage == 1) Green else MaterialTheme.colorScheme.background)
                        .padding(10.dp)
                    ) {
                        Text(
                            text = "Second Team",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (pagerState.currentPage == 1) Color.White else Green
                            )
                        )
                    }
                }
            }
        HorizontalPager(state = pagerState)  { page ->
            when (page) {
                0 -> TeamList(team = firstTeam ?: emptyList())
                1 -> TeamList(team = secondTeam ?: emptyList())
            }
        }
    }
}

@Composable
fun TeamList(team: List<PlayerData>) {
    Column(modifier = Modifier.padding(8.dp)) {
        team.forEach { player ->
            Text(text = player.firstName + " " + player.lastName)
        }
    }
}