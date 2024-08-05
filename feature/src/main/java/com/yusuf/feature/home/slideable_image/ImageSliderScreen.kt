package com.yusuf.feature.home.slideable_image

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.yusuf.feature.R
import com.yusuf.theme.GREEN
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSliderScreen() {

    val imagesList = listOf(
        R.drawable.adv_1,
        R.drawable.adv_2,
        R.drawable.adv_3,
        R.drawable.adv_4
    )

    val pageState = rememberPagerState(pageCount = { imagesList.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(10000)
            coroutineScope.launch {
                val nextPage = (pageState.currentPage + 1) % imagesList.size
                pageState.scrollToPage(nextPage)
            }
        }
    }

    Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {

        HorizontalPager(state = pageState) { page ->

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(horizontal = 10.dp)
                        .graphicsLayer {
                            val pageOffset = pageState.offsetForPage(page)
                            val offScreenRight = pageOffset < 0f
                            val deg = 105f
                            val interpolated = FastOutLinearInEasing.transform(pageOffset.absoluteValue)
                            rotationY = min(interpolated * if (offScreenRight) deg else -deg, 90f)

                            transformOrigin = TransformOrigin(
                                pivotFractionX = if (offScreenRight) 0f else 1f,
                                pivotFractionY = .5f
                            )
                        }

                ) {
                    Image(
                        painter = painterResource(id = imagesList[page]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
        }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(top = 270.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.Center
        ) {

            repeat(pageState.pageCount) { iteration ->
                val color = if (pageState.currentPage == iteration) GREEN else Color.Gray
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(10.dp)
                )

            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction
