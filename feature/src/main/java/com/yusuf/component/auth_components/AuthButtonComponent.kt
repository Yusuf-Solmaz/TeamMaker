package com.yusuf.component.auth_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.theme.DARK_GREEN


@Composable
fun AuthButtonComponent(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fillMaxWidth: Boolean = true,
    heightIn: Dp = 48.dp
) {
    Button(
        onClick = { onClick() },
        modifier = modifier
            .then(if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
            .heightIn(heightIn),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Red)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(heightIn)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            APPBAR_GREEN,
                            DARK_GREEN
                        )
                    ),
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}