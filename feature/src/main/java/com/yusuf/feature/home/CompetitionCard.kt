package com.yusuf.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.feature.R
import com.yusuf.theme.DARK_RED
import com.yusuf.theme.RED

@Composable
fun CompetitionCard(
    competition: CompetitionData,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp)
    ) {
        Box (
            modifier = Modifier.fillMaxSize()
                .clickable {
                    onClick()
                }
        ){
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
                    value = "Update",
                    onClick = onUpdate,
                    fillMaxWidth = false,
                    modifier = Modifier.width(80.dp),
                    heightIn = 37.dp
                )

                AuthButtonComponent(
                    value = "Delete",
                    onClick = onDelete,
                    fillMaxWidth = false,
                    modifier = Modifier.width(80.dp),
                    heightIn = 37.dp,
                    firstColor = RED,
                    secondColor = DARK_RED
                )

            }
        }
    }
}