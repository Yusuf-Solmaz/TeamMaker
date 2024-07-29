package com.yusuf.component.auth_components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClickableLoginTextComponent(tryToLogin: Boolean = true, onTextSelected: (String) -> Unit) {
    val initialText = if (tryToLogin) "Already have an account? " else "Don't have an account yet? "
    val loginText = if (tryToLogin) "Login" else "Register"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        pushStringAnnotation(tag = "ACTION", annotation = loginText)
        withStyle(style = SpanStyle(color = Color.Gray, fontWeight = FontWeight.Bold)) {
            append(loginText)
        }
    }

    ClickableText(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center,

            ),
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "ACTION", start = offset, end = offset)
                .firstOrNull()?.also { span ->
                    Log.d("ClickableTextComponent", "{${span.item}}")
                    onTextSelected(span.item)
                }
        }
    )
}