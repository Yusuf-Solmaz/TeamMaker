package com.yusuf.feature.saved_competitions

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.component.ConfirmationDialog
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.feature.R
import com.yusuf.navigation.NavigationGraph

@Composable
fun CompetitionCard(
    competition: SavedCompetitionsModel,
    onDeleteClick: (SavedCompetitionsModel) -> Unit,
    navController: NavController,
    padding: Dp = 8.dp,
) {
    var shouldShowItemDeletionDialog by remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.image_loading))

    Card(
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth()
            .clickable {
                val route = NavigationGraph.getSavedCompetitionDetailsRoute(competition)
                navController.navigate(route)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(padding),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(8.dp)
                        .clip(CircleShape),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = competition.imageUrl.ifEmpty { R.drawable.no_image },
                        contentDescription = "Competition Image",
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop,
                        loading = {
                            LottieAnimation(
                                composition,
                                modifier = Modifier.size(100.dp),
                                iterations = LottieConstants.IterateForever
                            )
                        }
                    )
                }
            }


            // Text Section
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)  // Remove extra padding to align with other elements
            ) {

                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 24.dp)
                ) {

                    Text(
                        text = competition.competitionName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }


                Spacer(modifier = Modifier.height(4.dp))

                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Location: ${competition.locationName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Time
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_watch_later_24),
                        contentDescription = "Time Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Time: ${competition.competitionTime}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Date
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date Icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Date: ${competition.competitionDate}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Delete Icon
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    tint = Color.Red,
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            shouldShowItemDeletionDialog = true
                        }
                )

                if (shouldShowItemDeletionDialog) {
                    ConfirmationDialog({
                        shouldShowItemDeletionDialog = it
                    }, {
                        onDeleteClick(competition)
                    })
                }
            }
        }
    }
}

